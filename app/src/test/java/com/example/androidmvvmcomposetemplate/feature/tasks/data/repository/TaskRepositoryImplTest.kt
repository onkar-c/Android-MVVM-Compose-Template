package com.example.androidmvvmcomposetemplate.feature.tasks.data.repository

import app.cash.turbine.test
import com.example.androidmvvmcomposetemplate.core.data.dao.TaskDao
import com.example.androidmvvmcomposetemplate.core.data.local.TaskEntity
import com.example.androidmvvmcomposetemplate.feature.tasks.data.remote.TaskApi
import com.example.androidmvvmcomposetemplate.feature.tasks.data.remote.TaskDto
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TaskRepositoryImplTest {

    private lateinit var taskDao: TaskDao
    private lateinit var taskApi: TaskApi
    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        taskDao = mockk(relaxed = true)
        taskApi = mockk(relaxed = true)
        repository = TaskRepositoryImpl(taskDao, taskApi)
    }


    @Test
    fun `observeTasks emits mapped domain tasks`() = runTest {
        // Arrange
        val entities = listOf(
            TaskEntity(id = 1L, title = "E1", isDone = false, createdAt = 1L),
            TaskEntity(id = 2L, title = "E2", isDone = true, createdAt = 2L)
        )
        every { taskDao.observeTasks() } returns flowOf(entities)

        // Act + Assert using Turbine
        repository.observeTasks().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals("E1", items[0].title)
            assertEquals(false, items[0].isDone)
            assertEquals("E2", items[1].title)
            assertEquals(true, items[1].isDone)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTask ignores blank title`() = runTest {
        repository.addTask("   ")

        coVerify(exactly = 0) { taskDao.insertTask(any()) }
    }

    @Test
    fun `addTask inserts task when title is valid`() = runTest {
        repository.addTask("New Task")

        coVerify(exactly = 1) { taskDao.insertTask(any()) }
    }

    @Test
    fun `refreshFromRemote clears and inserts tasks`() = runTest {
        val apiTasks = listOf(
            TaskDto(userId = 1L, id = 10L, title = "Remote 1", completed = false),
            TaskDto(userId = 1L, id = 11L, title = "Remote 2", completed = true)
        )
        coEvery { taskApi.getTasks() } returns apiTasks

        repository.refreshFromRemote()

        coVerify(exactly = 1) { taskDao.clearAll() }
        coVerify(exactly = 1) { taskDao.insertTasks(any()) }
    }
}

