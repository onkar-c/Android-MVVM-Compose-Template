package com.example.androidmvvmcomposetemplate.feature.tasks.data.remote

import com.example.androidmvvmcomposetemplate.core.data.local.TaskEntity
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task

data class TaskDto(
    val userId: Long,
    val id: Long,
    val title: String,
    val completed: Boolean,      // common naming pattern from APIs

)


// --- Mapping functions ---

fun TaskDto.toEntity(): TaskEntity =
    TaskEntity(
        id = id,
        title = title,
        isDone = completed,
        createdAt =  System.currentTimeMillis()
    )

fun TaskDto.toDomain(): Task =
    Task(
        id = id,
        title = title,
        isDone = completed,
        createdAt = System.currentTimeMillis()
    )