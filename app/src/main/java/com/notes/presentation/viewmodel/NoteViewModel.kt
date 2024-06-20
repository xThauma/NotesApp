package com.notes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.event.NoteUiEvent
import com.notes.data.repository.NoteCloudFirestoreRepositoryImpl
import com.notes.domain.usecase.FilterAndSortNotesUseCase
import com.notes.domain.repository.NoteRepository
import com.notes.domain.model.Note
import com.notes.domain.model.SortOrder
import com.notes.presentation.ui.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NoteViewModel @Inject constructor(
        private val noteRepository: NoteRepository,
        private val noteCloudFirestoreRepositoryImpl: NoteCloudFirestoreRepositoryImpl,
        private val filterAndSortNotesUseCase: FilterAndSortNotesUseCase,
) : ViewModel() {

    private val _toastMessage = MutableSharedFlow<String?>()
    val toastMessage: MutableSharedFlow<String?> = _toastMessage

    private val _noteState = MutableStateFlow(NoteState())
    val noteState: StateFlow<NoteState> = _noteState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _sortQuery = MutableStateFlow(SortOrder.ASCENDING)
    val sortQuery: StateFlow<SortOrder> = _sortQuery

    init {
        viewModelScope.launch {
            combine(
                    _sortQuery,
                    _searchQuery
            ) { sortOrder, searchQuery ->
                Pair(
                        sortOrder,
                        searchQuery
                )
            }.flatMapLatest { (sortOrder, searchQuery) ->
                noteRepository.getAllNotes()
                    .map { notes ->
                        filterAndSortNotesUseCase.execute(
                                notes,
                                searchQuery,
                                sortOrder
                        )
                    }
            }
                .collect { filteredNotes ->
                    _noteState.update { currentState ->
                        currentState.copy(
                                allNotes = filteredNotes,
                                isLoading = false
                        )
                    }
                }
        }
    }

    fun getNoteById(noteId: Int?) {
        viewModelScope.launch {
            _noteState.update { currentState ->
                currentState.copy(
                        isLoading = true
                )
            }
            noteRepository.getNoteById(noteId)
                .catch {
                    Log.e(
                            "NoteViewModel",
                            "Error fetching note by id",
                            it
                    )
                }
                .collect { note ->
                    _noteState.update { currentState ->
                        currentState.copy(
                                currentNote = note,
                                isLoading = false
                        )
                    }
                }
        }
    }

    fun setSearchQuery(searchQuery: String) {
        if (_searchQuery.value != searchQuery) {
            _searchQuery.value = searchQuery
            _noteState.update { currentState ->
                currentState.copy(
                        isLoading = true
                )
            }
        }
    }

    private fun setSortOrder(sortOrder: SortOrder) {
        if (_sortQuery.value != sortOrder) {
            _sortQuery.value = sortOrder
            _noteState.update { currentState ->
                currentState.copy(
                        isLoading = true
                )
            }
        }
    }

    fun onEvent(event: NoteUiEvent) {
        when (event) {
            is NoteUiEvent.Delete -> deleteNote(note = event.note)
            is NoteUiEvent.Insert -> insertNote(note = event.note)
            is NoteUiEvent.Update -> updateNote(note = event.note)
            is NoteUiEvent.GetById -> getNoteById(noteId = event.noteId)
            is NoteUiEvent.FilterEvent.TextChange -> setSearchQuery(searchQuery = event.searchQuery)
            is NoteUiEvent.SortEvent.Ascending -> setSortOrder(sortOrder = SortOrder.ASCENDING)
            is NoteUiEvent.SortEvent.Descending -> setSortOrder(sortOrder = SortOrder.DESCENDING)
        }
    }

    private fun insertNote(note: Note) = viewModelScope.launch {
        noteCloudFirestoreRepositoryImpl.insertNote(note)
        noteRepository.insertNote(note)
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        noteCloudFirestoreRepositoryImpl.updateNote(note)
        noteRepository.updateNote(note)
    }

    private fun deleteNote(note: Note) = viewModelScope.launch {
        noteCloudFirestoreRepositoryImpl.deleteNote(note)
        noteRepository.deleteNote(note)
    }
}
