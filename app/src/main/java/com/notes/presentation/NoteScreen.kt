package com.notes.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.notes.data.event.NoteUiEvent
import com.notes.domain.model.Note
import com.notes.presentation.viewmodel.NoteViewModel

@Composable
fun NotesScreen(
    noteViewModel: NoteViewModel,
    navigateToEditNote: (Int) -> Unit
) {
    val noteState by noteViewModel.noteState.collectAsState()
    val snackbarMessage by noteViewModel.toastMessage.collectAsState(initial = null)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column {
            if (noteState.isLoading) {
                NotesLoading()
            } else {
                NotesList(
                    paddingBottom = paddingValues.calculateBottomPadding(),
                    noteList = noteState.allNotes,
                    onEvent = { event -> noteViewModel.onEvent(event) },
                    navigateToEditNote = navigateToEditNote
                )
            }
        }
    }
}

@Composable
fun NotesLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun NotesList(
    paddingBottom: Dp,
    noteList: List<Note>,
    onEvent: (NoteUiEvent) -> Unit,
    navigateToEditNote: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(count = 2),
        modifier = Modifier.padding(bottom = paddingBottom)
    ) {
        items(noteList, key = { it.id }) { note ->
            NoteItem(
                modifier = Modifier
                    .animateItem(tween(1000, 0, FastOutSlowInEasing))
                    .heightIn(max = 340.dp),
                note = note,
                onEvent = onEvent,
                navigateToEditNote = navigateToEditNote
            )
        }
    }
}