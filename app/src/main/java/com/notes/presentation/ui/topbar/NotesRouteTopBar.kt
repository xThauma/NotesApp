package com.notes.presentation.ui.topbar

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.notes.data.event.NoteUiEvent
import com.notes.data.event.auth.AuthUiEvent
import com.notes.presentation.ui.auth.SignInState
import com.notes.presentation.ui.topbar.icons.NoteAddIcon
import com.notes.presentation.ui.topbar.icons.NoteAuthIcon
import com.notes.presentation.ui.topbar.icons.NoteSearchIcon
import com.notes.presentation.ui.topbar.icons.NoteSortIcon

@Composable
fun NotesRouteTopBar(
    searchVisible: Boolean,
    searchText: String,
    authState: SignInState,
    onEvent: (NoteUiEvent) -> Unit,
    onAuthEvent: (AuthUiEvent) -> Unit,
    navigateToAddNote: () -> Unit,
    navigateToProfile: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchClosed: () -> Unit,
    onSearchClicked: () -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data ?: return@rememberLauncherForActivityResult
                    onAuthEvent(AuthUiEvent.SignInResultEvent(intent = intent, navigation = navigateToProfile))
                }
            })

    if (searchVisible) {
        NoteSearchBar(searchText = searchText,
            onSearchTextChanged = {
                onSearchTextChanged(it)
            },
            onSearchClose = {
                onSearchClosed()
            })
    } else {
        NoteSearchIcon(onSearchClick = { onSearchClicked() })
        NoteAddIcon { navigateToAddNote.invoke() }
        NoteSortIcon { onEvent(it) }
        NoteAuthIcon(authState = authState,
            onSignInEvent = {
                onAuthEvent(AuthUiEvent.SignInClickEvent {
                    if (it == null) {
                        return@SignInClickEvent
                    }
                    launcher.launch(
                        IntentSenderRequest.Builder(it)
                            .build()
                    )
                })
            },
            navigateToProfile = navigateToProfile)
    }
}

