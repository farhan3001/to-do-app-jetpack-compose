package com.chexup.test.todoapp.domain.model

import android.os.Parcelable
import com.chexup.test.todoapp.data.local.entity.TodoEntity
import com.chexup.test.todoapp.data.remote.dto.TodoDto
import kotlinx.parcelize.Parcelize

/**
 * Domain model class yang merepresentasikan Todo entity
 * Digunakan sebagai data class utama di layer presentation dan domain
 */
@Parcelize
data class Todo(
    val id: String,
    val taskName: String,
    val taskDescription: String,
    val dateCreated: String,
    val dateEnded: String,
    val isCompleted: Boolean = false,
    val isSynced: Boolean = false
) : Parcelable

fun Todo.toEntity(): TodoEntity {
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

fun Todo.toDto(): TodoDto {
    return TodoDto(
        id = id,
        taskName = taskName,
        taskDescription = taskDescription,
        dateCreated = dateCreated,
        dateEnded = dateEnded,
        isCompleted = isCompleted,
    )
}