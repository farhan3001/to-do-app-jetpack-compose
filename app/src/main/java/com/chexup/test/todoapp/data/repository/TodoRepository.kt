package com.chexup.test.todoapp.data.repository


import com.chexup.test.todoapp.data.local.database.TodoDao
import com.chexup.test.todoapp.data.local.entity.TodoEntity
import com.chexup.test.todoapp.data.remote.api.TodoApiService
import com.chexup.test.todoapp.data.remote.dto.TodoDto
import com.chexup.test.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Repository class yang mengimplementasikan single source of truth pattern
 * Mengelola data dari local database dan remote API dengan strategi offline-first
 */
@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val apiService: TodoApiService
) {

    fun getAllTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getTodoById(id: String): Todo? {
        return todoDao.getTodoById(id)?.toDomainModel()
    }

    suspend fun insertTodo(todo: Todo) {
        todoDao.insertTodo(todo.toEntity())
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo.toEntity())
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo.toEntity())
    }

    suspend fun deleteTodoById(id: String) {
        todoDao.deleteTodoById(id)
    }

    suspend fun syncTodosFromRemote(): Result<List<Todo>> {
        return try {
            val response = apiService.getAllTodos()
            if (response.isSuccessful) {
                val remoteTodos = response.body()?.map { it.toDomainModel() } ?: emptyList()
                val entities = remoteTodos.map { it.copy(isSynced = true).toEntity() }
                todoDao.insertTodos(entities)
                Result.success(remoteTodos)
            } else {
                Result.failure(Exception("Failed to fetch todos: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncTodoToRemote(todo: Todo): Result<Todo> {
        return try {
            val response = if (todo.id.isEmpty()) {
                apiService.createTodo(todo.toDto())
            } else {
                apiService.updateTodo(todo.id, todo.toDto())
            }

            if (response.isSuccessful) {
                val syncedTodo = response.body()?.toDomainModel()?.copy(isSynced = true)
                syncedTodo?.let {
                    todoDao.insertTodo(it.toEntity())
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to sync todo: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTodoFromRemote(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteTodo(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete todo: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun TodoEntity.toDomainModel(): Todo {
    return Todo(
        id = id,
        taskName = taskName,
        taskDescription = taskDescription,
        dateCreated = dateCreated,
        dateEnded = dateEnded,
        isCompleted = isCompleted,
        isSynced = isSynced
    )
}

private fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        id = id,
        taskName = taskName,
        taskDescription = taskDescription,
        dateCreated = dateCreated,
        dateEnded = dateEnded,
        isCompleted = isCompleted,
        isSynced = isSynced
    )
}

private fun TodoDto.toDomainModel(): Todo {
    return Todo(
        id = id,
        taskName = taskName,
        taskDescription = taskDescription,
        dateCreated = dateCreated,
        dateEnded = dateEnded,
        isCompleted = false,
        isSynced = true
    )
}

private fun Todo.toDto(): TodoDto {
    return TodoDto(
        id = id,
        taskName = taskName,
        taskDescription = taskDescription,
        dateCreated = dateCreated,
        dateEnded = dateEnded
    )
}