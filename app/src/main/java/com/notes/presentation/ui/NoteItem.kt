package com.notes.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.notes.data.ColorResource
import com.notes.domain.model.Note


@Composable
fun NoteItem(
        modifier: Modifier,
        note: Note,
        navigateToEditNote: (Int) -> Unit,
) {
    ElevatedCard(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            navigateToEditNote(note.id)
        }) {
        Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = ColorResource.colorList[note.selectedColorIndex])
        ) {
            Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
            ) {
                Text(
                        text = note.title,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )
                Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.heightIn(max = 150.dp),
                        overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}