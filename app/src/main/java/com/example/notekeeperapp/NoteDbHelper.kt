package com.example.notekeeperapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class NoteDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Companion object holds constants for the database schema
    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_CONTENT TEXT," +
                "$COLUMN_TIMESTAMP TEXT)"
        db?.execSQL(createTableQuery)
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Gets the current formatted timestamp
    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    // --- CRUD Operations ---

    // Create: Add a new note
    fun addNote(title: String, content: String) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
            put(COLUMN_TIMESTAMP, getCurrentTimestamp())
        }
        val db = writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Read: Get all notes
    fun getAllNotes(): List<Note> {
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                )
                noteList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return noteList
    }

    // Update: Modify an existing note
    fun updateNote(id: Long, newTitle: String, newContent: String) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, newTitle)
            put(COLUMN_CONTENT, newContent)
            put(COLUMN_TIMESTAMP, getCurrentTimestamp()) // Update timestamp on edit
        }
        val db = writableDatabase
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    // Delete: Remove a note
    fun deleteNote(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}
