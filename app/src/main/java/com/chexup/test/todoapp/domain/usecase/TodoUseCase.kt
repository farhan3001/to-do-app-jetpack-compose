package com.chexup.test.todoapp.domain.usecase

import com.chexup.test.todoapp.data.repository.TodoRepository
import com.chexup.test.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case class yang mengencapsulate business logic untuk operasi todo
 * Bertindak sebagai mediator antara ViewModel dan Repository
 */
@Singleton
class TodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {

    fun getAllTodos(): Flow<List<Todo>> {
        return repository.getAllTodos()
    }

    suspend fun addTodo(
        taskName: String,
        taskDescription: String,
        dateEnded: String
    ): Result<Unit> {
        return try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
            val todo = Todo(
                id = UUID.randomUUID().toString(),
                taskName = taskName.trim(),
                taskDescription = taskDescription.trim(),
                dateCreated = currentDate,
                dateEnded = dateEnded,
                isCompleted = false,
                isSynced = false
            )

            repository.insertTodo(todo)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleTodoComplete(todo: Todo): Result<Unit> {
        return try {
            val updatedTodo = todo.copy(isCompleted = !todo.isCompleted, isSynced = false)
            repository.updateTodo(updatedTodo)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTodo(todo: Todo): Result<Unit> {
        return try {
            repository.deleteTodo(todo)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncWithRemote(): Result<List<Todo>> {
        return repository.syncTodosFromRemote()
    }

    fun validateTodoInput(taskName: String, taskDescription: String): Boolean {
        return taskName.trim().isNotEmpty() && taskDescription.trim().isNotEmpty()
    }
}