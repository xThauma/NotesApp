package com.notes.presentation.viewmodel.auth

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.event.auth.AuthUiEvent
import com.notes.domain.model.auth.UserData
import com.notes.domain.repository.AuthRepository
import com.notes.presentation.ui.auth.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
        private val authRepository: AuthRepository,
) : ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: MutableSharedFlow<String> = _toastMessage

    init {
        viewModelScope.launch {
            getUser()?.run {
                _signInState.update { signInState ->
                    signInState.copy(userData = this)
                }
                delay(500)
                _toastMessage.emit("Successfully signed in to $username")
            }
        }
    }

    fun onEvent(event: AuthUiEvent) {
        return when (event) {
            is AuthUiEvent.SignInResultEvent -> onSignInResult(intent = event.intent)
            is AuthUiEvent.SignOutEvent -> onSignOutResult()
            is AuthUiEvent.SignInClickEvent -> onSignInClick(onLaunch = event.onLaunch)
        }
    }

    private fun getUser(): UserData? = authRepository.getSignedInUser()

    private fun onSignInResult(intent: Intent) {
        viewModelScope.launch {
            val result = authRepository.getSignInResult(
                    intent = intent
            )
            _signInState.update {
                it.copy(
                        userData = result.userData,
                        signInError = result.errorMessage
                )
            }
            if (result.userData != null) {
                _toastMessage.emit("Successfully signed in to ${result.userData.username}")
            } else {
                _toastMessage.emit("Signed out")
            }
        }
    }

    private fun onSignOutResult() {
        viewModelScope.launch {
            _signInState.update {
                SignInState()
            }
            _toastMessage.emit("Signed out")
        }
    }

    private fun onSignInClick(onLaunch: (IntentSender?) -> Unit) {
        viewModelScope.launch {
            onLaunch(authRepository.signIn())
        }
    }
}