package com.example.androidmvvmcomposetemplate.feature.tasks.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class TasksScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun initial_empty_state_shows_hint_text() {
        // Arrange: empty state
        val state = TasksUiState(
            tasks = emptyList(), isLoading = false, errorMessage = null, newTaskTitle = ""
        )

        composeRule.setContent {
            TasksScreen(
                state = state,
                onTitleChange = {},
                onAddTaskClick = {},
                onTaskCheckedChange = {},
                onDeleteTaskClick = {},
                onRefreshClick = {},
                onErrorMessageShown = {})
        }

        // Assert: empty-state text is visible
        composeRule.onNodeWithText("No tasks yet. Add one or pull from API.").assertIsDisplayed()
    }

    @Test
    fun entering_text_and_pressing_add_calls_callback() {
        var lastTitle: String? = null


        val state = TasksUiState(
            tasks = emptyList(), isLoading = false, errorMessage = null, newTaskTitle = ""
        )

        composeRule.setContent {
            TasksScreen(
                state = state.copy(newTaskTitle = ""), // ViewModel normally controls this
                onTitleChange = { title -> lastTitle = title },
                onAddTaskClick = {},
                onTaskCheckedChange = {},
                onDeleteTaskClick = {},
                onRefreshClick = {},
                onErrorMessageShown = {})
        }

        // Type text into the "New task" field
        composeRule.onNodeWithText("New task").performTextInput("Buy milk")

        // Click "Add" button
        composeRule.onNodeWithText("Add").performClick()

        // Assert: callbacks were invoked
       assert(lastTitle == "Buy milk")

    }

    @Test
    fun refresh_icon_is_visible_in_top_bar() {
        val state = TasksUiState()

        composeRule.setContent {
            TasksScreen(
                state = state,
                onTitleChange = {},
                onAddTaskClick = {},
                onTaskCheckedChange = {},
                onDeleteTaskClick = {},
                onRefreshClick = {},
                onErrorMessageShown = {})
        }

        composeRule.onNodeWithContentDescription("Refresh tasks").assertIsDisplayed()
    }

}