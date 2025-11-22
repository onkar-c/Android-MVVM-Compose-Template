package com.example.androidmvvmcomposetemplate.feature.tasks.ui

import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.repository.TaskRepository
import com.example.androidmvvmcomposetemplate.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepository = FakeTaskRepository()

    @Test
    fun `initial state collects tasks from repository`() = runTest {
        // Arrange
        fakeRepository.setTasks(
            listOf(
                Task(id = 1, title = "T1"),
                Task(id = 2, title = "T2")
            )
        )

        // Act
        val viewModel = TasksViewModel(fakeRepository)
        advanceUntilIdle() // let ViewModel collect from Flow

        // Assert
        val state = viewModel.uiState.value
        assertEquals(2, state.tasks.size)
        assertEquals("T1", state.tasks[0].title)
    }

    @Test
    fun `onNewTaskTitleChange updates state`() = runTest {
        val viewModel = TasksViewModel(fakeRepository)

        viewModel.onNewTaskTitleChange("Hello")

        assertEquals("Hello", viewModel.uiState.value.newTaskTitle)
    }

    @Test
    fun `onAddTaskClicked with blank title sets errorMessage`() = runTest {
        val viewModel = TasksViewModel(fakeRepository)

        viewModel.onNewTaskTitleChange("  ")
        viewModel.onAddTaskClicked()

        val state = viewModel.uiState.value
        assert(state.errorMessage != null)
    }

    @Test
    fun `onAddTaskClicked with valid title calls repository and clears field`() = runTest {
        val viewModel = TasksViewModel(fakeRepository)

        viewModel.onNewTaskTitleChange("New Task")
        viewModel.onAddTaskClicked()
        advanceUntilIdle()

        assertEquals("New Task", fakeRepository.lastAddedTitle)
        assertEquals("", viewModel.uiState.value.newTaskTitle)
    }
}

// ---- Fake repository for ViewModel tests ----

private class FakeTaskRepository : TaskRepository {

    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    var lastAddedTitle: String? = null
        private set

    override fun observeTasks(): Flow<List<Task>> = tasksFlow

    override suspend fun addTask(title: String) {
        lastAddedTitle = title
        val newTask = Task(
            id = (tasksFlow.value.maxOfOrNull { it.id } ?: 0L) + 1,
            title = title
        )
        tasksFlow.value = tasksFlow.value + newTask
    }

    override suspend fun toggleTaskDone(id: Long) {
        tasksFlow.value = tasksFlow.value.map {
            if (it.id == id) it.copy(isDone = !it.isDone) else it
        }
    }

    override suspend fun deleteTask(id: Long) {
        tasksFlow.value = tasksFlow.value.filterNot { it.id == id }
    }

    override suspend fun refreshFromRemote() {
        // no-op for tests
    }

    fun setTasks(tasks: List<Task>) {
        tasksFlow.value = tasks
    }
}