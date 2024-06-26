package com.notes.data.di

import android.content.Context
import androidx.room.Room
import com.notes.data.database.NoteDao
import com.notes.data.database.NoteDatabase
import com.notes.data.repository.NoteFirestoreRepositoryImpl
import com.notes.data.repository.NoteRoomRepositoryImpl
import com.notes.domain.repository.NoteCloudFirestoreRepository
import com.notes.domain.repository.NoteRoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "notes_database" // Replace with your database name
        ).build()
    }

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao() // Assuming your database has a noteDao() function
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRoomRepository {
        return NoteRoomRepositoryImpl(noteDao)
    }

    @Provides
    @Singleton
    fun provideCloudFirestoreRepository() : NoteCloudFirestoreRepository {
        return NoteFirestoreRepositoryImpl()
    }
}