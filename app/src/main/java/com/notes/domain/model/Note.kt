package com.notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
data class Note(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val title: String,
        val content: String,
        val creationDate: LocalDate = LocalDate.now(),
        val selectedColorIndex: Int = 0
) {

    init {
        require(title.isNotBlank()) { "Title cannot be empty" }
        require(content.isNotBlank()) { "Content cannot be empty" }
    }

    val formattedCreationDate: String
        get() = creationDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))

    companion object {

        internal const val DATE_FORMAT: String = "dd/MM/yyyy"
    }
}