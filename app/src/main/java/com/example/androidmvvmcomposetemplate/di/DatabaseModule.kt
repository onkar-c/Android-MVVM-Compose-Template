package com.example.androidmvvmcomposetemplate.di

import android.content.Context
import androidx.room.Room
import com.example.androidmvvmcomposetemplate.core.data.AppDatabase
import com.example.androidmvvmcomposetemplate.core.data.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            // For a template / dev: easier. In real prod you’d add migrations instead.
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideTaskDao(
        database: AppDatabase
    ): TaskDao = database.taskDao()
}