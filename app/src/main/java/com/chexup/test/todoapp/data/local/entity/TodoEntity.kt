package com.chexup.test.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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