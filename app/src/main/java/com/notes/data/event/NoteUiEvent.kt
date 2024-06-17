package com.notes.data.event

import com.notes.domain.model.Note

sealed interface NoteUiEvent {

    data class Delete(val note: Note) : NoteUiEvent
    data class Insert(val note: Note) : NoteUiEvent
    data class Update(val note: Note) : NoteUiEvent
    data class UpdateNotification(val note: Note) : NoteUiEvent
    data class GetById(val noteId: Int?) : NoteUiEvent
    sealed interface SortEvent : NoteUiEvent {
        data object Ascending : SortEvent
        data object Descending : SortEvent
    }
    sealed interface FilterEvent : NoteUiEvent {
        data class TextChange(val searchQuery: String) : FilterEvent
    }
}