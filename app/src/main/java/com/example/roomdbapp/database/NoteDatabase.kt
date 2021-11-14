package com.example.roomdbapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import androidx.room.Room


@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DataRoomConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
    fun cleanUp() {
        noteDB = null
    }

    companion object {
        private var noteDB: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase? {
            if (noteDB == null) {
                noteDB = buildDatabaseInstance(context)
            }
            return noteDB
        }

        private fun buildDatabaseInstance(context: Context): NoteDatabase {
            return Room.databaseBuilder(context, NoteDatabase::class.java, Constants.DB_NAME)
                .allowMainThreadQueries().build()
        }
    }
}