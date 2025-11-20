package com.example.androidmvvmcomposetemplate.feature.tasks.data.repository

import com.example.androidmvvmcomposetemplate.core.data.dao.TaskDao
import com.example.androidmvvmcomposetemplate.feature.tasks.data.mappers.toDomain
import com.example.androidmvvmcomposetemplate.feature.tasks.data.mappers.toEntity
import com.example.androidmvvmcomposetemplate.feature.tasks.data.remote.TaskApi
import com.example.androidmvvmcomposetemplate.feature.tasks.data.remote.toEntity
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApi: TaskApi
) : TaskRepository {

    override fun observeTasks(): Flow<List<Task>> {
        return taskDao.observeTasks().map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun addTask(title: String) {
        if (title.isBlank()) return

        val newTask = Task(title = title.trim())
        taskDao.insertTask(newTask.toEntity())
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteById(id)
    }

    override suspend fun refreshFromRemote() {
        // 1. Fetch from API
        val remoteTasks = taskApi.getTasks().take(20)

        // 2. Map DTO -> Entity
        val entities = remoteTasks.map { it.toEntity() }

        // 3. Replace local cache
        taskDao.clearAll()
        taskDao.insertTasks(entities)
    }


    override suspend fun toggleTaskDone(id: Long) {
        taskDao.toggleDoneById(id)
    }
}