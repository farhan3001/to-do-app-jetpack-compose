package com.chexup.test.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chexup.test.todoapp.domain.model.Todo

/**
 * Entity class untuk Room database yang merepresentasikan struktur tabel todo
 * Menyimpan data todo secara lokal dengan semua field yang diperlukan
 */
@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey
    val id: String,
    val taskName: String,
    val taskDescription: String,
    val dateCreated: String,
    val dateEnded: String,
    val isCompleted: Boolean = false,
    val isSynced: Boolean = false
)

fun TodoEntity.toDomainModel(): Todo {
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