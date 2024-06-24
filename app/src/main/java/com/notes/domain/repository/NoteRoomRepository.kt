package com.notes.domain.repository

import com.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRoomRepository : NoteRepository {
    fun getNoteById(noteId: Int?): Flow<Note?>
}