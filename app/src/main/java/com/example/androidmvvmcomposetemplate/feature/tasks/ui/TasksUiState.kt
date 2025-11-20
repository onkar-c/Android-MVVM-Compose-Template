package com.example.androidmvvmcomposetemplate.feature.tasks.ui

import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val newTaskTitle: String = ""
)
