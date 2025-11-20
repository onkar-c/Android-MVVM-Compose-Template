package com.example.androidmvvmcomposetemplate.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidmvvmcomposetemplate.core.data.dao.TaskDao
import com.example.androidmvvmcomposetemplate.core.data.local.TaskEntity


@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}