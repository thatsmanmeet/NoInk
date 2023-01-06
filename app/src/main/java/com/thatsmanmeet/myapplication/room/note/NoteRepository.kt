package com.thatsmanmeet.myapplication.room.note

import androidx.lifecycle.LiveData
import com.thatsmanmeet.myapplication.room.note.Note
import com.thatsmanmeet.myapplication.room.note.NoteDao

class NoteRepository(private val noteDao: NoteDao) {

    fun getAllNotes() : LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note){
        noteDao.insert(note)
    }

    suspend fun deleteNote(note: Note){
        noteDao.delete(note)
    }

    suspend fun updateNote(note: Note){
        noteDao.update(note)
    }

}