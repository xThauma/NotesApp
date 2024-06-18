package com.notes.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.notes.data.ColorResource
import com.notes.data.event.NoteUiEvent
import com.notes.domain.model.Note
import com.notes.presentation.viewmodel.NoteViewModel

@Composable
fun UpdateNote(
        navController: NavHostController,
        noteViewModel: NoteViewModel = hiltViewModel(),
        onEvent: (NoteUiEvent) -> Unit,
        noteId: Int? = null,
) {
    val noteState by noteViewModel.noteState.collectAsState()

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf("") }
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var showError by remember { mutableStateOf(false) }
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(ColorResource.colorList[selectedColorIndex]) }
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
            selectedColorIndex = note.selectedColorIndex
            dominantColor = ColorResource.colorList[selectedColorIndex]
            buttonText = "Update Note"
        }
        title = title.copy(
                selection = TextRange(title.text.length)
        )
    }

    UpdateNoteContainerElement(
            defaultDominantColor = defaultDominantColor,
            dominantColor = dominantColor
    ) {
        Column(
                modifier = Modifier
                    .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                    .fillMaxSize()
        ) {
            UpdateTitleElement(
                    title = title.text,
                    onValueChange = { text -> title = TextFieldValue(text); showError = false },
                    showError = showError
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            UpdateContentElement(
                    content = content,
                    onValueChange = { content = it; showError = false },
                    showError = showError
            )

            Spacer(modifier = Modifier.height(20.dp))

            UpdateColorIndicatorElement(selectedColorIndex = selectedColorIndex,
                    onColorSelected = { index ->
                        selectedColorIndex = index
                        dominantColor = ColorResource.colorList[index]
                    })

            Spacer(modifier = Modifier.height(10.dp))

            UpdateNoteButtonElement(title = title.text,
                    content = content,
                    buttonText = buttonText,
                    onEvent = {
                        note?.let {
                            onEvent(
                                    NoteUiEvent.Update(
                                            note = it.copy(
                                                    title = title.text,
                                                    content = content,
                                                    selectedColorIndex = selectedColorIndex
                                            )
                                    )
                            )
                        } ?: run {
                            onEvent(
                                    NoteUiEvent.Insert(
                                            Note(
                                                    title = title.text,
                                                    content = content,
                                                    selectedColorIndex = selectedColorIndex
                                            )
                                    )
                            )
                        }
                        navController.popBackStack()
                    },
                    onError = { showError = true })

        }
    }
}

@Composable
fun UpdateNoteButtonElement(
        title: String,
        content: String,
        buttonText: String,
        onEvent: () -> Unit,
        onError: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
                onClick = {
                    if (title.isBlank() || content.isBlank()) {
                        onError()
                    } else {
                        onEvent()
                    }
                }) {
            Text(buttonText)
        }
    }
}

@Composable
fun UpdateNoteContainerElement(
        defaultDominantColor: Color,
        dominantColor: Color,
        content: @Composable () -> Unit
) {
    Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(
                        brush = Brush.verticalGradient(
                                colors = listOf(
                                        defaultDominantColor,
                                        dominantColor
                                )
                        )
                )
    ) {
        content()
    }
}

@Composable
fun UpdateColorIndicatorElement(
        selectedColorIndex: Int,
        onColorSelected: (Int) -> Unit
) {
    LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(minSize = 36.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        itemsIndexed(ColorResource.colorList) { index, color ->
            Box(modifier = Modifier
                .size(40.dp)
                .border(
                        if (selectedColorIndex == index) 3.dp else 1.dp,
                        Color.Black,
                        CircleShape
                )
                .clip(CircleShape)
                .background(color)
                .clickable {
                    onColorSelected(index)
                })
        }
    }
}

@Composable
fun UpdateTitleElement(
        title: String,
        onValueChange: (String) -> Unit,
        showError: Boolean
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = title,
            onValueChange = { text -> onValueChange(text) },
            label = { Text("Title") },
            isError = showError && title.isBlank(),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
            )
    )
    if (showError && title.isBlank()) {
        Text(
                text = "Title cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun UpdateContentElement(
        content: String,
        onValueChange: (String) -> Unit,
        showError: Boolean
) {
    TextField(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            value = content,
            onValueChange = { onValueChange(it) },
            label = { Text("Content") },
            isError = showError && content.isBlank(),
            shape = RoundedCornerShape(24.dp),
            maxLines = 3,
            colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
            )
    )
    if (showError && content.isBlank()) {
        Text(
                text = "Content cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateTitleElementPreview() {
    val titleState = remember { mutableStateOf("Sample Title") }
    val contentState = remember { mutableStateOf("Sample Content") }
    val showErrorState = remember { mutableStateOf(false) }
    val selectedColorIndex = 1

    UpdateNoteContainerElement(
            defaultDominantColor = MaterialTheme.colorScheme.surface,
            dominantColor = ColorResource.colorList[selectedColorIndex]
    ) {
        Column(
                modifier = Modifier.padding(16.dp)
        ) {
            UpdateTitleElement(
                    title = titleState.value,
                    onValueChange = { titleState.value = it },
                    showError = showErrorState.value
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            UpdateContentElement(
                    content = contentState.value,
                    onValueChange = { contentState.value = it },
                    showError = showErrorState.value
            )
            Spacer(modifier = Modifier.height(20.dp))
            UpdateColorIndicatorElement(selectedColorIndex,
                    {})
            UpdateNoteButtonElement(
                    title = titleState.value,
                    content = contentState.value,
                    buttonText = "Update note",
                    {},
                    {})
        }
    }
}