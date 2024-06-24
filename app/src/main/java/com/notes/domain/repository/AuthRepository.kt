package com.notes.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.notes.domain.model.auth.AuthResult
import com.notes.domain.model.auth.UserData


/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
interface AuthRepository {

    suspend fun signIn() : IntentSender?
    suspend fun signOut()
    suspend fun getSignInResult(intent: Intent): AuthResult
    fun getSignedInUser(): UserData?
}