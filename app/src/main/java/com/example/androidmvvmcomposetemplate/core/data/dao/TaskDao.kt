package com.example.androidmvvmcomposetemplate.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidmvvmcomposetemplate.core.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE tasks SET isDone = :isDone WHERE id = :id")
    suspend fun updateTaskDone(id: Long, isDone: Boolean)

    @Query("UPDATE tasks SET isDone = NOT isDone WHERE id = :id")
    suspend fun toggleDoneById(id: Long)

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}