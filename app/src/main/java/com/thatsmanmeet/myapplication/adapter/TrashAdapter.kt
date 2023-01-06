package com.thatsmanmeet.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.room.trash.Trash


class TrashAdapter(private val context: Context, private val listener: ITrashRVAdapter) : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {
    val allTrashNotes = ArrayList<Trash>()
    inner class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.notesTitle)
        var description: TextView = itemView.findViewById(R.id.notesDescription)
        var dateBox: TextView = itemView.findViewById(R.id.tvNotesDate)
        val noteCard : ConstraintLayout = itemView.findViewById(R.id.NoteCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashViewHolder {
        val viewHolder =
            TrashViewHolder(LayoutInflater.from(context).inflate(R.layout.trash_item, parent, false))
        viewHolder.noteCard.setOnClickListener {
            listener.onNoteCardClicked(allTrashNotes[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
        val currentNote = allTrashNotes[position]
        holder.title.text = currentNote.title
        holder.description.text = currentNote.description
        holder.dateBox.text = currentNote.date
    }

    override fun getItemCount(): Int {
        return allTrashNotes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:List<Trash>){
        allTrashNotes.clear()
        allTrashNotes.addAll(newList)
        notifyDataSetChanged()
    }

}

interface ITrashRVAdapter{

    fun onNoteCardClicked(trash: Trash){

    }
}