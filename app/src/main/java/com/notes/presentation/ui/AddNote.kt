package com.notes.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.notes.data.ColorResource
import com.notes.data.event.NoteUiEvent
import com.notes.domain.model.Note
import com.notes.domain.model.Note.Companion.DATE_FORMAT
import com.notes.presentation.viewmodel.NoteViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateNote(
    navController: NavHostController,
    noteViewModel: NoteViewModel = hiltViewModel(),
    noteId: Int? = null,
) {
    val noteState by noteViewModel.noteState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf("") }
    var notifyDate by remember { mutableStateOf(LocalDate.now()) }
    var shouldNotify by remember { mutableStateOf(false) }
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var showError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(ColorResource.colorList[selectedColorIndex]) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().toEpochDay().times(24 * 60 * 60 * 1000)
    )
    var buttonText by remember {
        mutableStateOf("Add Note")
    }

    LaunchedEffect(key1 = noteId) {
        noteViewModel.getNoteById(noteId = noteId)
    }


    val note = noteState.currentNote
    LaunchedEffect(note) {
        note?.let {
            title = TextFieldValue(note.title)
            content = note.content
            notifyDate = note.notifyDate ?: LocalDate.now()
            shouldNotify = note.shouldNotify
            selectedColorIndex = note.selectedColorIndex
            dominantColor = ColorResource.colorList[selectedColorIndex]
            buttonText = "Update Note"
        }
        title = title.copy(
            selection = TextRange(title.text.length)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        defaultDominantColor,
                        dominantColor
                    )
                )
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                value = title,
                onValueChange = { title = it; showError = false },
                label = { Text("Title") },
                isError = showError && title.text.isBlank(),
            )
            if (showError && title.text.isBlank()) {
                Text(
                    text = "Title cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = content,
                onValueChange = { content = it; showError = false },
                label = { Text("Content") },
                isError = showError && content.isBlank()
            )
            if (showError && content.isBlank()) {
                Text(
                    text = "Content cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(ColorResource.colorList) { index, color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                if (selectedColorIndex == index) 3.dp else 1.dp,
                                Color.Black,
                                CircleShape
                            )
                            .clip(CircleShape)
                            .background(color)
                            .clickable {
                                selectedColorIndex = index
                                dominantColor = ColorResource.colorList[selectedColorIndex]
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { showDatePicker = true }
                ) {
                    Text(
                        text = notifyDate?.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                            ?: "Select Notify Date"
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Should Notify")
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = shouldNotify,
                        onCheckedChange = {
                            shouldNotify = it
                            if (!it) {
                                notifyDate = null
                                showDateError = false
                            }
                        }
                    )
                }
            }

            if (showDateError && shouldNotify) {
                Text(
                    text = "Please select a notify date",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let {
                                notifyDate = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                            }
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    },
                    content = {
                        datePickerState.selectedDateMillis =
                            notifyDate?.toEpochDay()?.times(24 * 60 * 60 * 1000)
                        DatePicker(state = datePickerState)
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (title.text.isBlank() || content.isBlank() || (shouldNotify && notifyDate == null)) {
                        showError = true
                        if (shouldNotify && notifyDate == null) {
                            showDateError = true
                        }
                    } else {
                        note?.let {
                            noteViewModel.onEvent(
                                NoteUiEvent.Update(
                                    note = it.copy(
                                        title = title.text,
                                        content = content,
                                        notifyDate = notifyDate,
                                        shouldNotify = shouldNotify,
                                        selectedColorIndex = selectedColorIndex
                                    )
                                )
                            )
                        } ?: run {
                            noteViewModel.onEvent(
                                NoteUiEvent.Insert(
                                    Note(
                                        title = title.text,
                                        content = content,
                                        notifyDate = notifyDate,
                                        shouldNotify = shouldNotify,
                                        selectedColorIndex = selectedColorIndex
                                    )
                                )
                            )
                        }
                        navController.popBackStack()
                    }
                }
            ) {
                Text(buttonText)
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}