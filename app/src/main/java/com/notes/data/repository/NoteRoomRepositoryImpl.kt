package com.notes.data.repository

import com.notes.data.database.NoteDao
import com.notes.domain.repository.NoteRoomRepository
import com.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRoomRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRoomRepository {

    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override fun getNoteById(noteId: Int?): Flow<Note?> = noteDao.getNoteById(noteId)

    override suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}