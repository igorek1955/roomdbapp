package com.example.roomdbapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.advancedviews.cards.adapters.NoteAdapter
import com.example.roomdbapp.database.NoteDatabase
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.roomdbapp.database.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference


class NoteListActivity : AppCompatActivity(), NoteAdapter.OnNoteItemClick {

    // Variables and the widgets
    lateinit var textViewMsg: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var noteDatabase: NoteDatabase
    lateinit var notes: ArrayList<Note>
    lateinit var notesAdapter: NoteAdapter
    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFab()
        initVars()
        displayList()
    }

    private fun displayList() {
        RetrieveTask(this).execute()
    }

    private fun initVars() {
        textViewMsg = findViewById(R.id.tv__empty)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notes = ArrayList()
        notesAdapter = NoteAdapter(notes, this)
        recyclerView.adapter = notesAdapter
        noteDatabase = NoteDatabase.getInstance(this)!!
    }

    private fun setFab() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {view ->
            Snackbar.make(view, "Menu", Snackbar.LENGTH_LONG)
                .setAction("Add note", View.OnClickListener {
                    val intent = Intent(this, AddNoteActivity::class.java)
                    startActivityForResult(intent, 100)
                })
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                val note = data?.getSerializableExtra("note") as Note
                if (note != null) {
                    notes.add(note)
                }
            } else if (resultCode == 2) {
                notes[position] = (data?.getSerializableExtra("note") as Note?)!!
            }
            listVisibility()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun listVisibility() {
        var emptyMsgVisibility = View.GONE
        if (notes.size == 0) {
            // No Items to Display
            if (textViewMsg.visibility == View.GONE) emptyMsgVisibility = View.VISIBLE
        }
        textViewMsg.visibility = emptyMsgVisibility
        notesAdapter.notifyDataSetChanged()
    }

    override fun onNoteClick(pos: Int) {
        AlertDialog.Builder(this)
            .setItems(arrayOf("Delete", "Update"), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when (which) {
                        0 -> {
                            noteDatabase.getNoteDao().deleteNote(notes[pos])
                            notes.removeAt(pos)
                            listVisibility()
                        }
                        1 -> {
                            position = pos
                            val intent = Intent(this@NoteListActivity, AddNoteActivity::class.java)
                            intent.putExtra("note", notes[pos])
                            startActivityForResult(intent, 100)
                        }
                    }
                }
            }).show()
    }

    inner class RetrieveTask(val context: NoteListActivity) {
        private var notes: List<Note> = ArrayList()
        private val activityReference: WeakReference<NoteListActivity> = WeakReference(context)
        private var execSuccess: Boolean = false

        fun execute() {
            object : BackgroundTask(context) {
                override fun doInBackground() {
                    notes =
                        activityReference.get()?.noteDatabase?.getNoteDao()?.getNotes() as ArrayList<Note>
                    execSuccess = true
                }

                override fun onPostExecute() {
                    if (execSuccess && notes.isNotEmpty()) {
                        activityReference.get()?.notes?.clear();
                        activityReference.get()?.notes?.addAll(notes);

                        // Hide the empty textView ;
                        activityReference.get()?.textViewMsg?.setVisibility(View.GONE);
                        activityReference.get()?.notesAdapter?.notifyDataSetChanged();
                    }
                }

            }.execute();
        }
    }

    override fun onDestroy() {
        noteDatabase.cleanUp()
        super.onDestroy()
    }
}