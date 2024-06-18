package com.notes.presentation.ui

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.notes.presentation.navigation.Routes
import com.notes.presentation.viewmodel.NoteViewModel

@Composable
fun NoteAppNavigation(
        noteViewModel: NoteViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    Scaffold(topBar = {
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentDestination = currentBackStackEntry?.destination
        val title = when (currentDestination?.route) {
            Routes.NOTES -> "Notes"
            Routes.UPDATE_NOTE -> "Edit Note"
            Routes.ADD_NOTE -> "Add Note"
            else -> "Notes App"
        }
        AppTopBar(
                title = title,
                navController = navController,
                onEvent = { event -> noteViewModel.onEvent(event) },
                navigateToAddNote = {
                    navController.navigate("addNote/null")
                },
        )
    }) { paddingValues ->
        NavHost(
                navController = navController,
                startDestination = Routes.NOTES,
                modifier = Modifier.padding(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        top = paddingValues.calculateTopPadding(),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {
            composable(Routes.NOTES) {
                NotesScreen(
                        navigateToEditNote = { noteId ->
                            navController.navigate("updateNote/$noteId")
                        },
                        noteViewModel = noteViewModel
                )
            }
            composable(
                    route = Routes.UPDATE_NOTE,
                    arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                val noteIdInt = noteId?.toIntOrNull()
                UpdateNote(
                        navController = navController,
                        onEvent = { event -> noteViewModel.onEvent(event) },
                        noteId = noteIdInt
                )
            }
            composable(
                    route = Routes.ADD_NOTE
            ) { _ ->
                UpdateNote(
                        navController = navController,
                        onEvent = { event -> noteViewModel.onEvent(event) },
                        noteId = null
                )
            }
        }
    }
}
