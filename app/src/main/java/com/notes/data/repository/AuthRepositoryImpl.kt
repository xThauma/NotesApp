package com.notes.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.notes.R
import com.notes.domain.model.auth.AuthResult
import com.notes.domain.model.auth.UserData
import com.notes.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
        @ApplicationContext private val context: Context,
        private val signInClient: SignInClient
) : AuthRepository {

    private val auth = Firebase.auth

    override suspend fun signIn(): IntentSender? {
        val result = try {
            signInClient.beginSignIn(getSignInRequest())
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e else null

        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signOut() {
        try {
            signInClient.signOut()
                .await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override suspend fun getSignInResult(intent: Intent): AuthResult {
        val credentials = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credentials.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(
                googleIdToken,
                null
        )
        return try {
            val user = auth.signInWithCredential(googleCredentials)
                .await().user
            AuthResult(userData = user?.run {
                UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e else null
            AuthResult(errorMessage = e.message)
        }
    }

    override fun getSignedInUser(): UserData? {
        return auth.currentUser?.run {
            UserData(
                    userId = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString()
            )
        }
    }

    private fun getSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                    GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(context.getString(R.string.web_client_id))
                        .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}