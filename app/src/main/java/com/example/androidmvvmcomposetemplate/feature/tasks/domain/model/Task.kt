package com.example.androidmvvmcomposetemplate.feature.tasks.domain.model

data class Task(val id: Long = 0L,
                val title: String,
                val isDone: Boolean = false,
                val createdAt: Long = System.currentTimeMillis()
)
