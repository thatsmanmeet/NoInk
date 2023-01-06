package com.thatsmanmeet.myapplication.room.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.thatsmanmeet.myapplication.room.todo.Todo
import com.thatsmanmeet.myapplication.room.todo.TodoDatabase
import com.thatsmanmeet.myapplication.room.todo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : TodoRepository
    val allTodos : LiveData<List<Todo>>

    init {
        val dao = TodoDatabase.getDatabaseInstance(application).getTodoDao()
        repository = TodoRepository(dao)
        allTodos = repository.getAllTodos()
    }

     fun insertTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todo)
        }
    }

    fun updateTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }

}