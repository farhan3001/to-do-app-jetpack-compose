package com.chexup.test.todoapp.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chexup.test.todoapp.domain.model.Todo
import com.chexup.test.todoapp.domain.usecase.TodoUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class yang mengelola UI state dan business logic untuk Todo screen
 * Menggunakan StateFlow untuk reactive state management dan Hilt untuk dependency injection
 */
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoUseCase: TodoUseCase
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _showAddDialog = mutableStateOf(false)
    val showAddDialog: State<Boolean> = _showAddDialog

    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    init {
        loadTodos()
        syncWithRemote()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            todoUseCase.getAllTodos().collect { todoList ->
                _todos.value = todoList
            }
        }
    }

    fun addTodo(taskName: String, taskDescription: String, dateEnded: String) {
        if (!todoUseCase.validateTodoInput(taskName, taskDescription)) {
            _error.value = "Task name and description cannot be empty"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            todoUseCase.addTodo(taskName, taskDescription, dateEnded).fold(
                onSuccess = {
                    _showAddDialog.value = false
                    _error.value = null
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to add todo"
                }
            )
            _isLoading.value = false


        }
    }

    fun toggleTodoComplete(todo: Todo) {
        viewModelScope.launch {
            todoUseCase.toggleTodoComplete(todo).fold(
                onSuccess = {
                    _error.value = null
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to toggle todo"
                }
            )
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            _isLoading.value = true
            todoUseCase.deleteTodo(todo).fold(
                onSuccess = {
                    _error.value = null
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to delete todo"
                }
            )
            _isLoading.value = false
        }
    }

    fun syncWithRemote() {
        viewModelScope.launch {
            _isRefreshing.value = true
            todoUseCase.syncWithRemote().fold(
                onSuccess = {
                    _error.value = null
                },
                onFailure = { exception ->
                    _error.value = "Sync failed: ${exception.message}"
                }
            )
            _isRefreshing.value = false
            if (_isLoading.value) {
                _isLoading.value = false
            }

        }
    }

    fun showAddDialog() {
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    fun clearError() {
        _error.value = null
    }
}