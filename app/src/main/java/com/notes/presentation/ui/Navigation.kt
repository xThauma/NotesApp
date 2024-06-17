package com.notes.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.notes.presentation.viewmodel.NoteViewModel

@Composable
fun NoteAppNavigation(
    noteViewModel: NoteViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            val currentBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentDestination = currentBackStackEntry?.destination
            val title = when (currentDestination?.route) {
                "notes" -> "Notes"
                "updateNote/{noteId}" -> {
                    val noteId = currentBackStackEntry.arguments?.getString("noteId")
                    if (noteId == "null") "Add Note" else "Edit Note"
                }

                else -> "Notes App"
            }
            AppTopBar(
                title = title,
                navController = navController,
                onEvent = { event -> noteViewModel.onEvent(event) },
                navigateToAddNote = {
                    navController.navigate("updateNote/null")
                },
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "notes",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("notes") {
                NotesScreen(
                    navigateToEditNote = { noteId ->
                        navController.navigate("updateNote/$noteId")
                    },
                    noteViewModel = noteViewModel
                )
            }
            composable(
                route = "updateNote/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                val noteIdInt = noteId?.toIntOrNull()
                UpdateNote(navController = navController, noteId = noteIdInt)
            }
        }
    }
}
