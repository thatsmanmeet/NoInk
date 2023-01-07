package com.thatsmanmeet.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.room.note.Note


class NotesAdapter(
    private val context: Context,
    private val listener: INotesRVAdapter,
    @LayoutRes private val layout : Int,
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    val allNotes = ArrayList<Note>()
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.notesTitle)
        var description: TextView = itemView.findViewById(R.id.notesDescription)
        var dateBox: TextView = itemView.findViewById(R.id.tvNotesDate)
        val noteCard : ConstraintLayout = itemView.findViewById(R.id.NoteCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder =
            NoteViewHolder(LayoutInflater.from(context).inflate(layout, parent, false))

        viewHolder.noteCard.setOnClickListener {
            listener.onCardClicked(allNotes[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = allNotes[position]
        holder.title.text = currentNote.title
        holder.description.text = currentNote.description
        holder.dateBox.text = currentNote.date
        try{
            holder.noteCard.setBackgroundColor(ContextCompat.getColor(context,currentNote.backgroundColor))
        }catch (e:Exception){
            holder.noteCard.setBackgroundColor(ContextCompat.getColor(context,R.color.notes_card))
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun updateList(newList:List<Note>){
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

}

interface INotesRVAdapter{
    fun onNoteDeleteButtonClicked(note: Note){

    }

    fun onCardClicked(note: Note){

    }
}