package com.example.roomdbapp.database

import androidx.room.*


@Dao
interface NoteDao {
    @Query("SELECT * FROM " + Constants.TABLE_NAME_NOTE)
    fun getNotes(): List<Note>
    @Insert
    fun saveNote(note: Note): Long
    @Delete
    fun deleteNote(note: Note)
    @Update
    fun updateNote(note: Note)
    @Delete
    fun deleteNotes(vararg note: Note?)
}