package com.notes.presentation.ui

import com.notes.domain.model.Note

data class NoteState(
    val allNotes: List<Note> = emptyList(),
    val currentNote: Note? = null,
    val isLoading: Boolean = true,
)