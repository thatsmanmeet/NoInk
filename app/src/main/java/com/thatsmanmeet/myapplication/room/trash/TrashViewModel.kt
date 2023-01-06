package com.thatsmanmeet.myapplication.room.trash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.thatsmanmeet.myapplication.room.trash.Trash
import com.thatsmanmeet.myapplication.room.trash.TrashDatabase
import com.thatsmanmeet.myapplication.room.trash.TrashRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrashViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : TrashRepository
    val allTrashNotes : LiveData<List<Trash>>

    init {
        val dao = TrashDatabase.getTrashDatabaseInstance(application).getTrashDao()
        repository = TrashRepository(dao)
        allTrashNotes = repository.getAllNotes()
    }

    fun insertTrash(trash: Trash){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(trash)
        }
    }

    fun deleteTrash(trash: Trash){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(trash)
        }
    }

    fun clearTrash(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes()
        }
    }

}