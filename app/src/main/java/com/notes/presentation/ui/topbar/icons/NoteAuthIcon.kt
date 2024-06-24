package com.notes.presentation.ui.topbar.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.notes.presentation.ui.auth.SignInState

@Composable
fun NoteAuthIcon(
    authState: SignInState,
    onSignInEvent: () -> Unit,
    navigateToProfile: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .size(24.dp)
    ) {
        if (authState.isSignInSuccessful && authState.userData?.profilePictureUrl?.isBlank() == false) {
            AsyncImage(
                model = authState.userData.profilePictureUrl,
                contentDescription = "Image Description",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable {
                        navigateToProfile()
                    },
                contentScale = ContentScale.Fit,
            )
        } else {
            Icon(imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Auth Icon",
                modifier = Modifier.clickable {
                    onSignInEvent()
                })
        }
    }
}

