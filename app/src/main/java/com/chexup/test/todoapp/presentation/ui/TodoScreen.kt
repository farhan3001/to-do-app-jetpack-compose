package com.chexup.test.todoapp.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chexup.test.todoapp.presentation.ui.components.AddTodoDialog
import com.chexup.test.todoapp.presentation.ui.components.TodoItem
import com.chexup.test.todoapp.presentation.viewmodel.TodoViewModel

/**
 * Screen utama yang menampilkan daftar todo dengan fitur CRUD
 * Menggunakan LazyColumn untuk performance dan state management dengan ViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsState()
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val showAddDialog by viewModel.showAddDialog
    val isRefreshing by viewModel.isRefreshing

    LaunchedEffect(error) {
        error?.let {
            // Handle error display if needed
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo App") },
                actions = {
                    IconButton(
                        onClick = { viewModel.syncWithRemote() },
                        enabled = !isRefreshing
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = "Sync")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (todos.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No todos yet. Add one!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    items(
                        items = todos,
                        key = { it.id }
                    ) { todo ->
                        TodoItem(
                            todo = todo,
                            onToggleComplete = { viewModel.toggleTodoComplete(todo) },
                            onDelete = { viewModel.deleteTodo(todo) }
                        )
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (showAddDialog) {
        AddTodoDialog(
            onDismiss = { viewModel.hideAddDialog() },
            onConfirm = { title, description, endDate ->
                viewModel.addTodo(title, description, endDate)
            }
        )
    }

    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            // Show snackbar or handle error display
            viewModel.clearError()
        }
    }
}