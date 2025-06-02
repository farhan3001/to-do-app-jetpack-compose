package com.chexup.test.todoapp.domain.model

import android.os.Parcelable
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