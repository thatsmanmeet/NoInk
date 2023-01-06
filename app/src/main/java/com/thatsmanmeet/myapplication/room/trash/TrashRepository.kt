package com.thatsmanmeet.myapplication.room.trash

import androidx.lifecycle.LiveData
import com.thatsmanmeet.myapplication.room.trash.Trash
import com.thatsmanmeet.myapplication.room.trash.TrashDao

class TrashRepository(private val trashDao: TrashDao) {

    fun getAllNotes() : LiveData<List<Trash>> = trashDao.getAllNotes()

    suspend fun insertNote(trash: Trash){
        trashDao.insert(trash)
    }

    suspend fun deleteNote(trash: Trash){
        trashDao.delete(trash)
    }

    fun deleteAllNotes(){
        trashDao.deleteAllNotes()
    }

}