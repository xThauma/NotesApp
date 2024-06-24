package com.notes.domain.usecase.firestore

import com.notes.domain.model.Note
import com.notes.domain.repository.NoteCloudFirestoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesFirestoreUseCase @Inject constructor(private val noteRepository: NoteCloudFirestoreRepository) {

    fun execute() : Flow<List<Note>> {
        return noteRepository.getAllNotes()
    }
}