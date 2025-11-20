package com.example.androidmvvmcomposetemplate.feature.tasks.domain.repository

import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

/*
This is the contract between domain/UI and the data layer.
ViewModel (and use cases, if you add them) will depend on this interface, not on DAO / Room directly.
Internally, we’ll have a TaskRepositoryImpl that uses TaskDao + mappers.
 */

interface TaskRepository {
    fun observeTasks(): Flow<List<Task>>

    suspend fun addTask(title: String)

    suspend fun toggleTaskDone(id: Long)

    suspend fun deleteTask(id: Long)

    suspend fun refreshFromRemote()
}