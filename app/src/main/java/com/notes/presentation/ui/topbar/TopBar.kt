package com.notes.presentation.ui.topbar

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.notes.data.event.NoteUiEvent
import com.notes.presentation.navigation.Routes
import com.notes.presentation.viewmodel.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    navController: NavHostController,
    onEvent: (NoteUiEvent) -> Unit,
    navigateToAddNote: () -> Unit,
    navigateToProfile: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var searchVisible by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }
    val signInState by authViewModel.signInState.collectAsState()
    val toastMessage by authViewModel.toastMessage.collectAsState(initial = "")
    val context = LocalContext.current

    LaunchedEffect(key1 = toastMessage) {
        if (toastMessage.isNotBlank()) {
            Toast.makeText(
                context,
                toastMessage,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = if (navController.previousBackStackEntry != null) {
                Icons.AutoMirrored.Filled.ArrowBack
            } else {
                Icons.AutoMirrored.Filled.Assignment
            },
                contentDescription = "Navigation Icon",
                modifier = Modifier.clickable {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                })
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title)
        }
    },
        actions = {
            when (currentRoute) {
                Routes.NOTES -> {
                    NotesRouteTopBar(searchVisible = searchVisible,
                        searchText = searchText,
                        authState = signInState,
                        onEvent = onEvent,
                        onAuthEvent = { authViewModel.onEvent(it) },
                        navigateToAddNote = navigateToAddNote,
                        navigateToProfile = navigateToProfile,
                        onSearchTextChanged = {
                            searchText = it
                            onEvent(NoteUiEvent.FilterEvent.TextChange(it))
                        },
                        onSearchClosed = {
                            searchVisible = false
                            searchText = ""
                            onEvent(NoteUiEvent.FilterEvent.TextChange(searchText))
                        },
                        onSearchClicked = { searchVisible = true })
                }
            }
        })
}