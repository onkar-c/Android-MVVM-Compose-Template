package com.example.androidmvvmcomposetemplate.core.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidmvvmcomposetemplate.core.data.AppDatabase
import com.example.androidmvvmcomposetemplate.core.data.local.TaskEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries() // OK for tests
            .build()

        dao = db.taskDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insert_and_observe_tasks_returns_data() = runTest {
        val task = TaskEntity(
            id = 0L,
            title = "Test",
            isDone = false,
            createdAt = 123L
        )

        dao.insertTask(task)

        val tasks = dao.observeTasks().first()

        assertEquals(1, tasks.size)
        assertEquals("Test", tasks[0].title)
    }

    @Test
    fun deleteById_removes_task() = runTest {
        val task1 = TaskEntity(
            id = 1L,
            title = "T1",
            isDone = false,
            createdAt = 1L
        )
        val task2 = TaskEntity(
            id = 2L,
            title = "T2",
            isDone = false,
            createdAt = 2L
        )

        dao.insertTask(task1)
        dao.insertTask(task2)

        dao.deleteById(1L)

        val tasks = dao.observeTasks().first()
        assertEquals(1, tasks.size)
        assertEquals(2L, tasks[0].id)
    }
}