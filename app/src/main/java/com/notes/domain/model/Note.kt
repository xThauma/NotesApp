package com.notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

@Entity
@IgnoreExtraProperties
data class Note(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val title: String = "",
        val content: String = "",
        val creationDate: LocalDate = LocalDate.now(),
        val selectedColorIndex: Int = 0,
        val documentId: String = ""
) {

    constructor(
            title: String,
            content: String,
            creationDate: LocalDate = LocalDate.now(),
            selectedColorIndex: Int = 0
    ) : this(
            title = title,
            content = content,
            creationDate = creationDate,
            selectedColorIndex = selectedColorIndex,
            documentId = Firebase.firestore.collection("note")
                .document().id
    )

    init {
        require(title.isNotBlank()) { "Title cannot be empty" }
        require(content.isNotBlank()) { "Content cannot be empty" }
    }
}