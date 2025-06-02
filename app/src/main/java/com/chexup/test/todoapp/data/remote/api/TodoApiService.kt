package com.chexup.test.todoapp.data.remote.api

import com.chexup.test.todoapp.data.remote.dto.TodoDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface untuk Retrofit API service
 * Mendefinisikan endpoint-endpoint yang akan digunakan untuk komunikasi dengan Mock API
 */
interface TodoApiService {

    @GET("task-todo")
    suspend fun getAllTodos(): Response<List<TodoDto>>

    @GET("task-todo/{id}")
    suspend fun getTodoById(@Path("id") id: String): Response<TodoDto>

    @POST("task-todo")
    suspend fun createTodo(@Body todo: TodoDto): Response<TodoDto>

    @PUT("task-todo/{id}")
    suspend fun updateTodo(
        @Path("id") id: String,
        @Body todo: TodoDto
    ): Response<TodoDto>

    @DELETE("task-todo/{id}")
    suspend fun deleteTodo(@Path("id") id: String): Response<Unit>
}