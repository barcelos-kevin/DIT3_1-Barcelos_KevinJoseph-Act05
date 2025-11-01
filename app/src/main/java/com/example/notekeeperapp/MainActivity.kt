package com.example.notekeeperapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: NoteDbHelper
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize the Database Helper
        dbHelper = NoteDbHelper(this)

        // 2. Set up the RecyclerView
        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with click handlers
        notesAdapter = NotesAdapter(
            listOf(),
            onClick = { note ->
                // Open EditNoteActivity to view/edit the note
                val intent = Intent(this, EditNoteActivity::class.java).apply {
                    putExtra("note_id", note.id)
                    putExtra("note_title", note.title)
                    putExtra("note_content", note.content)
                }
                startActivity(intent)
            },
            onLongClick = { note ->
                // Show delete confirmation dialog
                AlertDialog.Builder(this)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Delete") { _, _ ->
                        dbHelper.deleteNote(note.id)
                        loadNotes()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        notesRecyclerView.adapter = notesAdapter

        // 3. Set up the Floating Action Button
        val fabAddNote: FloatingActionButton = findViewById(R.id.fabAddNote)
        fabAddNote.setOnClickListener {
            // Open EditNoteActivity to add a new note
            val intent = Intent(this, EditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 4. Load and display notes every time the activity resumes
        loadNotes()
    }

    private fun loadNotes() {
        val notes = dbHelper.getAllNotes()
        notesAdapter.updateData(notes)
    }
}
