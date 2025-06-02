package com.chexup.test.todoapp.data.remote.dto

import com.chexup.test.todoapp.domain.model.Todo
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object untuk API response
 * Merepresentasikan struktur data yang diterima dari Mock API
 */

data class TodoDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("taskName")
    val taskName: String,
    @SerializedName("taskDescription")
    val taskDescription: String,
    @SerializedName("dateCreated")
    val dateCreated: String,
    @SerializedName("dateEnded")
    val dateEnded: String,
    @SerializedName("isCompleted")
    val isCompleted: Boolean
)

fun TodoDto.toDomainModel(): Todo {
    return Todo(
        id = id,
        taskName = taskName,
        taskDescription = taskDescription,
        dateCreated = dateCreated,
        dateEnded = dateEnded,
        isCompleted = isCompleted,
        isSynced = true
    )
}