package com.notes.domain.usecase.database

import com.notes.domain.model.Note
import com.notes.domain.repository.NoteRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesRoomUseCase @Inject constructor(private val noteRepository: NoteRoomRepository) {

    fun execute() : Flow<List<Note>> {
        return noteRepository.getAllNotes()
    }
}