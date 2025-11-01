package com.example.notekeeperapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(
    private var notes: List<Note>,
    private val onClick: (Note) -> Unit,
    private val onLongClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    // Describes an item view and metadata about its place within the RecyclerView.
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val timestampTextView: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    // Called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.timestampTextView.text = note.timestamp
        holder.itemView.setOnClickListener { onClick(note) }
        holder.itemView.setOnLongClickListener {
            onLongClick(note)
            true
        }
    }

    // Returns the total number of items in the data set held by the adapter.
    override fun getItemCount(): Int = notes.size

    // Helper function to update the data in the adapter.
    fun updateData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged() // Refreshes the RecyclerView
    }
}
