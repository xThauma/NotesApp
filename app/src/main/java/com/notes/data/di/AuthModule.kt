package com.notes.data.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.notes.data.repository.AuthRepositoryImpl
import com.notes.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext


/**
 * Created by Kamil Lenartowicz on 06/18/2024 @ QVC, Inc
 */
@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindsFirebaseAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {

        @Provides
        fun provideSignInClient(@ApplicationContext context: Context): SignInClient = Identity.getSignInClient(context)
    }
}