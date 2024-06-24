package com.notes.presentation.ui.auth

import com.notes.domain.model.auth.UserData


/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
data class SignInState(
    val userData: UserData? = null,
    val signInError: String? = null,
    val isLoading: Boolean = false
) {
    val isSignInSuccessful: Boolean = userData != null
}