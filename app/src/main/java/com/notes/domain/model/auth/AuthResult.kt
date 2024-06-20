package com.notes.domain.model.auth

/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
data class AuthResult(
        val userData: UserData? = UserData.INVALID,
        val errorMessage: String? = null
)