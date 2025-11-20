package com.example.androidmvvmcomposetemplate.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidmvvmcomposetemplate.feature.tasks.ui.TasksRoute

/*
    rememberNavController() creates a NavController for the app.
    NavHost defines the navigation graph.
    Single screen now: "tasks" → calls TasksRoute().
 */
private object Destinations {
    const val TASKS = "tasks"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController, startDestination = Destinations.TASKS
    ) {
        composable(route = Destinations.TASKS) {
            TasksRoute()
        }
    }
}
