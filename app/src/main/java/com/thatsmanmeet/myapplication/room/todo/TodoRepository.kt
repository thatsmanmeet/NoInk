package com.thatsmanmeet.myapplication.room.todo

import androidx.lifecycle.LiveData
import com.thatsmanmeet.myapplication.room.todo.Todo
import com.thatsmanmeet.myapplication.room.todo.TodoDao

class TodoRepository(private val todoDao: TodoDao) {

    suspend fun insertTodo(todo: Todo){
        todoDao.insert(todo)
    }

    suspend fun updateTodo(todo: Todo){
        todoDao.update(todo)
    }

    suspend fun deleteTodo(todo: Todo){
        todoDao.delete(todo)
    }

    fun getAllTodos() : LiveData<List<Todo>> = todoDao.getAllTodos()

}