package com.thatsmanmeet.myapplication.adapter

import android.content.Context
import android.graphics.Paint
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.room.todo.Todo


class TodoAdapter(private val context: Context, private val listener: TodoInterface) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    val allTodo = ArrayList<Todo>()

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoText: TextView = itemView.findViewById(R.id.tvTodoText)
        val checkBox: CheckBox = itemView.findViewById(R.id.cbIsDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = allTodo[position]
        holder.todoText.text = currentTodo.todoText
        holder.checkBox.isChecked = currentTodo.isChecked
        if (currentTodo.isChecked) {
            holder.todoText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.todoText.paintFlags = 0
        }
        holder.checkBox.setOnClickListener {
            listener.onCheckBoxClicked(currentTodo)
            if (holder.checkBox.isChecked) {
                playSound()
                holder.todoText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.todoText.paintFlags = 0
            }
        }
        holder.itemView.setOnClickListener {
            listener.onTodoClicked(currentTodo)
        }
    }

    override fun getItemCount(): Int {
        return allTodo.size
    }

    fun updateList(newList: List<Todo>) {
        allTodo.clear()
        allTodo.addAll(newList)
        notifyDataSetChanged()
    }

    private fun playSound() {
        val mp = MediaPlayer.create(context.applicationContext, R.raw.done)
        mp.start()
        mp.setOnCompletionListener{
            it.stop()
            it.reset()
            it.release()
        }
    }
}

interface TodoInterface {
    fun onCheckBoxClicked(todo: Todo) {

    }
    fun onTodoClicked(todo: Todo){

    }
}
