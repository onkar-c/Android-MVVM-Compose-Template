package com.example.androidmvvmcomposetemplate.feature.tasks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/*
        1. @HiltViewModel + @Inject constructor(...)

        @HiltViewModel tells Hilt to manage this ViewModel’s lifecycle.
        TaskRepository gets injected from Hilt (thanks to RepositoryModule).

        2. MutableStateFlow → StateFlow

        _uiState is mutable but private.
        uiState is exposed as read-only StateFlow<TasksUiState> to UI.
        Compose will later collect this state and recompose when it changes.

        3. observeTasks() using Flow
        repository.observeTasks()
            .onStart { ... }   // set loading
            .catch { ... }     // handle errors
            .collect { tasks -> // update state with list }


        observeTasks() from repository returns a Flow<List<Task>>.
        Whenever the Room DB changes (insert/update/delete), the Flow emits a new list.
        We update _uiState with .update { it.copy(tasks = tasks) }.

        4. Event handlers

        onNewTaskTitleChange(newTitle)
        Updates state when user types in the text field.

        onAddTaskClicked()
        Validates title (not blank).

        Calls repository.addTask(title) in a coroutine.
        Clears newTaskTitle after success.

        onTaskCheckedChange(taskId)
        Called when user toggles checkbox.
        Calls repository.toggleTaskDone(taskId).

        onErrorMessageShown()
        UI can call this after showing error (snackbar/toast), to clear error from state.
*/

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
    }


    private fun observeTasks() {
        viewModelScope.launch {
            repository.observeTasks().onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }.catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, errorMessage = throwable.message ?: "Unknown error"
                        )
                    }
                }.collect { tasks ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, tasks = tasks, errorMessage = null
                        )
                    }
                }
        }
    }

    fun onNewTaskTitleChange(newTitle: String) {
        _uiState.update { state ->
            state.copy(newTaskTitle = newTitle)
        }
    }


    fun onAddTaskClicked() {
        val title = _uiState.value.newTaskTitle.trim()
        if (title.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Title cannot be empty")
            }
            return
        }

        viewModelScope.launch {
            try {
                repository.addTask(title)
                // Clear the text field on success
                _uiState.update {
                    it.copy(
                        newTaskTitle = "", errorMessage = null
                    )
                }
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(errorMessage = t.message ?: "Failed to add task")
                }
            }
        }
    }

    fun onTaskCheckedChange(taskId: Long) {
        // We ignore the new checked value and just toggle in repository
        viewModelScope.launch {
            try {
                repository.toggleTaskDone(taskId)
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(errorMessage = t.message ?: "Failed to update task")
                }
            }
        }
    }


    fun onDeleteTaskClicked(taskId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteTask(taskId)
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(errorMessage = t.message ?: "Failed to delete task")
                }
            }
        }
    }

    fun onRefreshClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {

                repository.refreshFromRemote()
                // observeTasks() flow will emit and clear isLoading
            } catch (t: Throwable) {
                t.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false, errorMessage = t.message ?: "Failed to refresh tasks"
                    )
                }
            }
        }
    }

    fun onErrorMessageShown() {
        // Call this from UI once you show a Snackbar/Toast
        _uiState.update { it.copy(errorMessage = null) }
    }

}