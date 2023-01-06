package com.thatsmanmeet.myapplication.room.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase() : RoomDatabase() {

    abstract fun getTodoDao() : TodoDao

    companion object{
        @Volatile
        var INSTANCE : TodoDatabase? = null

        fun getDatabaseInstance(context:Context) : TodoDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    TodoDatabase::class.java,"todo_db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}