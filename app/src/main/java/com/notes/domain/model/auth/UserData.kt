package com.notes.domain.model.auth


/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
data class UserData(
        val userId: String,
        val username: String?,
        val profilePictureUrl: String?
) {

    companion object {

        val INVALID = UserData(
                userId = "",
                username = "",
                profilePictureUrl = ""
        )
    }
}