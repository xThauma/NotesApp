package com.notes.domain

import com.notes.domain.model.Note
import com.notes.domain.model.SortOrder
import javax.inject.Inject

class FilterAndSortNotesUseCase @Inject constructor() {
    fun execute(notes: List<Note>, query: String, sortOrder: SortOrder): List<Note> {
        val filteredNotes = if (query.isEmpty()) {
            notes
        } else {
            notes.filter { it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true) }
        }

        return when (sortOrder) {
            SortOrder.ASCENDING -> filteredNotes.sortedBy { it.title }
            SortOrder.DESCENDING -> filteredNotes.sortedByDescending { it.title }
        }
    }
}