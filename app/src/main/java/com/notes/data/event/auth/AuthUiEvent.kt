package com.notes.data.event.auth

import android.content.Intent
import android.content.IntentSender


/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
sealed interface AuthUiEvent {


    data class SignInResultEvent(val intent: Intent): AuthUiEvent
    data class SignInClickEvent(val onLaunch: (IntentSender?) -> Unit): AuthUiEvent
    data object SignOutEvent: AuthUiEvent
}