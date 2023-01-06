package com.thatsmanmeet.myapplication.room.trash

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trash::class], version = 1, exportSchema = false)
abstract class TrashDatabase() : RoomDatabase() {

    abstract fun getTrashDao(): TrashDao

    companion object {
        @Volatile
        var INSTANCE: TrashDatabase? = null

         fun getTrashDatabaseInstance(context: Context): TrashDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrashDatabase::class.java,
                    "trash_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}