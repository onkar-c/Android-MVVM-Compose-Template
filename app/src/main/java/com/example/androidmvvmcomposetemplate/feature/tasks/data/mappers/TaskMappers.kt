package com.example.androidmvvmcomposetemplate.feature.tasks.data.mappers

import com.example.androidmvvmcomposetemplate.core.data.local.TaskEntity
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task
/*
    Keeps conversion logic in one place.
    Keeps repository implementation clean (just calls .toDomain() / .toEntity()).
 */
fun TaskEntity.toDomain(): Task =
    Task(
        id = id,
        title = title,
        isDone = isDone,
        createdAt = createdAt
    )

fun Task.toEntity(): TaskEntity =
    TaskEntity(
        id = id,
        title = title,
        isDone = isDone,
        createdAt = createdAt
    )