package com.notes.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.notes.data.ColorResource
import com.notes.data.event.NoteUiEvent
import com.notes.domain.model.Note


@Composable
fun NoteItem(
    modifier: Modifier,
    note: Note,
    onEvent: (NoteUiEvent) -> Unit,
    navigateToEditNote: (Int) -> Unit,
) {
    val defaultDominantColor =
        if (note.shouldNotify) Color(0xFFFFE4E1) else ColorResource.colorList[note.selectedColorIndex]
    val brush = Brush.verticalGradient(
        colors = listOf(
            defaultDominantColor,
            ColorResource.colorList[note.selectedColorIndex]
        )
    )
    val imageVector =
        if (note.shouldNotify) Icons.Default.Notifications else Icons.Default.NotificationsOff
    val contentDescription =
        if (note.shouldNotify) "Notification Active" else "Notification Inactive"

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigateToEditNote(note.id)
            }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = brush)
                    .padding(16.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(end = 40.dp),
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
            IconButton(
                onClick = {
                    onEvent(NoteUiEvent.UpdateNotification(note))
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                )
            }
        }
    }
}