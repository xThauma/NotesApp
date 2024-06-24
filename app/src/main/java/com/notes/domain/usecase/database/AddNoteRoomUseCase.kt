package com.notes.domain.usecase.database

import com.notes.domain.model.Note
import com.notes.domain.repository.NoteRoomRepository
import javax.inject.Inject

class AddNoteRoomUseCase @Inject constructor(private val noteRepository: NoteRoomRepository) {

    suspend fun execute(note: Note) {
        return noteRepository.insertNote(note)
    }
}