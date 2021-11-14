package com.example.roomdbapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import com.example.roomdbapp.database.Note
import com.example.roomdbapp.database.NoteDatabase
import com.google.android.material.textfield.TextInputEditText
import java.lang.Exception
import java.lang.ref.WeakReference
import java.security.AccessController.getContext

class AddNoteActivity : AppCompatActivity() {

    lateinit var etTitle: TextInputEditText
    lateinit var etContent: TextInputEditText
    lateinit var button: Button
    var note: Note? = null
    var update: Boolean = false

    lateinit var noteDb: NoteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note2)
        initVars()
        setContent()
        setupButtons()
    }

    private fun setupButtons() {
        button.setOnClickListener {
            if (update) {
                note?.setTitle(etTitle.text.toString())
                note?.setContent(etContent.text.toString())
                noteDb.getNoteDao().updateNote(note!!)
                setResult(note, 2)
            } else {
                note = Note(etContent.text.toString(), etTitle.text.toString())
                InsertTask(this, note!!).execute()
            }
        }
    }

    private fun setResult(note: Note?, flag: Int) {
        setResult(flag, Intent().putExtra("note", note!!))
        finish()
    }

    private fun setContent() {
        try {
            val noteFromIntent: Note? = intent.getSerializableExtra("note") as Note
            if (noteFromIntent != null) {
                supportActionBar!!.title = "Update Note"
                update = true
                button.text = "Update"
                etTitle.setText(noteFromIntent.getTitle())
                etContent.setText(noteFromIntent.getContent())
                note = noteFromIntent
            }
        } catch (e: Exception) {
            Log.e("error", "${e.message}")
        }

    }

    private fun initVars() {
        noteDb = NoteDatabase.getInstance(this)!!
        etTitle = findViewById(R.id.et_title)
        etContent = findViewById(R.id.et_content)
        button = findViewById(R.id.button_save)
    }

//    // Insert Task:
//    private class InsertTask(context: AddNoteActivity?, note: Note) :
//        AsyncTask<Void?, Void?, Boolean>() {
//        private val activityWeakReference: WeakReference<AddNoteActivity> = WeakReference(context)
//        private val note: Note = note
//
//        override fun onPostExecute(aBoolean: Boolean) {
//            if (aBoolean) {
//                activityWeakReference.get()?.setResult(note, 1)
//                activityWeakReference.get()?.finish()
//            }
//        }
//
//        // Do in background methods runs on a worker thread
//        override fun doInBackground(vararg params: Void?): Boolean {
    // retrieving auto incremented note id
//            val j: Long? = activityWeakReference.get()?.noteDb?.noteDao?.saveNote(note)
//            if (j != null) {
//                note.setNote_id(j)
//            }
//            return true
//        }
//    }

    private class InsertTask(val context: AddNoteActivity, val note: Note) {

        private val activityWeakReference: WeakReference<AddNoteActivity> = WeakReference(context)
        private var execSuccess: Boolean = false

        fun execute() {
            object : BackgroundTask(context) {
                override fun doInBackground() {
                    val j: Long? = activityWeakReference.get()?.noteDb?.getNoteDao()?.saveNote(note)
                    if (j != null) {
                        note.setNote_id(j)
                    }
                    execSuccess = true
                }

                override fun onPostExecute() {
                    if (execSuccess) {
                        activityWeakReference.get()?.setResult(note, 1)
                        activityWeakReference.get()?.finish()
                    }
                }

            }.execute();
        }
    }
}