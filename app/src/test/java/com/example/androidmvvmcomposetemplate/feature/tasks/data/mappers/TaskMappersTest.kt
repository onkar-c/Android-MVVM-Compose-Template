package com.example.androidmvvmcomposetemplate.feature.tasks.data.mappers

import com.example.androidmvvmcomposetemplate.core.data.local.TaskEntity
import com.example.androidmvvmcomposetemplate.feature.tasks.domain.model.Task
import org.junit.Assert.*
import org.junit.Test

class TaskMappersTest {

    @Test
    fun `entity to domain maps all fields correctly`() {
        // Arrange: create a TaskEntity (DB model)
        val entity = TaskEntity(
            id = 1L,
            title = "Test task",
            isDone = true,
            createdAt = 123456789L
        )

        // Act: map it to domain model
        val domain = entity.toDomain()

        // Assert: all values match
        assertEquals(1L, domain.id)
        assertEquals("Test task", domain.title)
        assertEquals(true, domain.isDone)
        assertEquals(123456789L, domain.createdAt)
    }


    @Test
    fun `domain to entity maps all fields correctly`() {
        // Arrange
        val task = Task(
            id = 42L,
            title = "Another task",
            isDone = false,
            createdAt = 987654321L
        )

        // Act
        val entity = task.toEntity()

        // Assert
        assertEquals(42L, entity.id)
        assertEquals("Another task", entity.title)
        assertEquals(false, entity.isDone)
        assertEquals(987654321L, entity.createdAt)
    }

}