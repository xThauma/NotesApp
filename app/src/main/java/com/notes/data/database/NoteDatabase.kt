package com.notes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.notes.data.converters.DateConverter
import com.notes.domain.model.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}