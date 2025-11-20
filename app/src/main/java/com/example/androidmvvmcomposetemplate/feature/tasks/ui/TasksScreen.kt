package com.example.androidmvvmcomposetemplate.feature.tasks.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task

/*
        TasksRoute: Knows about the ViewModel + DI (hiltViewModel()).
        Converts StateFlow → state for Compose.

        TasksScreen: Pure UI function: gets a state + callbacks.
        Easy to preview and test.
 */
@Composable
fun TasksRoute(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TasksScreen(
        state = uiState,
        onTitleChange = viewModel::onNewTaskTitleChange,
        onAddTaskClick = viewModel::onAddTaskClicked,
        onTaskCheckedChange = viewModel::onTaskCheckedChange,
        onDeleteTaskClick = viewModel::onDeleteTaskClicked,
        onRefreshClick = viewModel::onRefreshClicked,
        onErrorMessageShown = viewModel::onErrorMessageShown
    )
}


/*
        UI reads from state: TasksUiState only.

        It never talks to DB/Repository directly.

        User actions are turned into callbacks, which the ViewModel handles:

        typing → onTitleChange

        pressing add → onAddTaskClick

        toggling checkbox → onTaskCheckedChange

        That’s pure MVVM: UI ↔ ViewModel ↔ Repository ↔ DAO.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    state: TasksUiState,
    onTitleChange: (String) -> Unit,
    onAddTaskClick: () -> Unit,
    onTaskCheckedChange: (Long) -> Unit,
    onDeleteTaskClick: (Long) -> Unit,
    onRefreshClick: () -> Unit,
    onErrorMessageShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val error = state.errorMessage

    // Show error as Snackbar and then clear it in ViewModel
    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(error)
            onErrorMessageShown()
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Tasks") }, actions = {
            IconButton(
                onClick = onRefreshClick, enabled = !state.isLoading
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh, contentDescription = "Refresh tasks"
                )
            }
        })
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Input row
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.newTaskTitle,
                    onValueChange = onTitleChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("New task") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onAddTaskClick,
                    enabled = state.newTaskTitle.isNotBlank() && !state.isLoading
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                // Initial loading
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No tasks yet. Add one or pull from API.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = state.tasks, key = { it.id }) { task ->
                        TaskRow(
                            task = task,
                            onCheckedChange = { onTaskCheckedChange(task.id) },
                            onDeleteClick = { onDeleteTaskClick(task.id) })
                        HorizontalDivider(
                            Modifier, DividerDefaults.Thickness, DividerDefaults.color
                        )
                    }
                }
            }
        }
    }
}

// -------- Row for a single task --------

@Composable
fun TaskRow(
    task: Task, onCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isDone, onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (task.isDone) "Completed" else "Pending",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Filled.Delete, contentDescription = "Delete task"
            )
        }
    }

}
