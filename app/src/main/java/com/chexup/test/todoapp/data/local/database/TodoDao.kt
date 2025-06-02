package com.chexup.test.todoapp.data.local.database

import androidx.room.*
import com.chexup.test.todoapp.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk operasi database todo
 * Menyediakan metode CRUD dengan dukungan reactive programming melalui Flow
 */
@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY dateCreated DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: String): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<TodoEntity>)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodoById(id: String)

    @Query("DELETE FROM todos")
    suspend fun deleteAllTodos()

    @Query("SELECT * FROM todos WHERE isSynced = 0")
    suspend fun getUnsyncedTodos(): List<TodoEntity>
}