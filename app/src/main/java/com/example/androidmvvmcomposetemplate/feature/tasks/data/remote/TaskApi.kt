package com.example.androidmvvmcomposetemplate.feature.tasks.data.remote

import retrofit2.http.GET

interface TaskApi {

    @GET("todos")   // final URL: BASE_URL + "tasks"
    suspend fun getTasks(): List<TaskDto>
}