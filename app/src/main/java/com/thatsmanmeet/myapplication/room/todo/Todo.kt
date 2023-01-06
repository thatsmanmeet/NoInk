package com.thatsmanmeet.myapplication.room.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true) val ID : Long?,
    @ColumnInfo(name = "todo_text") val todoText : String,
    @ColumnInfo(name = "isCheckedTable") var isChecked: Boolean = false
)
