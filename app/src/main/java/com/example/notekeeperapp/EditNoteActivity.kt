package com.example.notekeeperapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditNoteActivity : AppCompatActivity() {

	private lateinit var dbHelper: NoteDbHelper
	private var noteId: Long? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_edit_note)

		dbHelper = NoteDbHelper(this)

		val titleEditText: EditText = findViewById(R.id.editTextTitle)
		val contentEditText: EditText = findViewById(R.id.editTextContent)
		val saveButton: Button = findViewById(R.id.buttonSave)

		noteId = intent.getLongExtra("note_id", -1L).let { if (it == -1L) null else it }
		intent.getStringExtra("note_title")?.let { titleEditText.setText(it) }
		intent.getStringExtra("note_content")?.let { contentEditText.setText(it) }

		saveButton.setOnClickListener {
			val title = titleEditText.text.toString().trim()
			val content = contentEditText.text.toString().trim()
			if (noteId == null) {
				dbHelper.addNote(title, content)
			} else {
				dbHelper.updateNote(noteId!!, title, content)
			}
			finish()
		}
	}
}



