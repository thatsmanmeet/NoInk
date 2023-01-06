package com.thatsmanmeet.myapplication.room.trash

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "trash_table")
data class Trash(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "notes_title") var title:String?,
    @ColumnInfo(name = "notes_description") var description:String?,
    @ColumnInfo(name = "notes_date") var date : String?
)
