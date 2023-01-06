package com.thatsmanmeet.myapplication.room.todo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.thatsmanmeet.myapplication.room.todo.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todo_table order by ID ASC")
    fun getAllTodos() : LiveData<List<Todo>>

}