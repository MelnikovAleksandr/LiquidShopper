package ru.asmelnikov.liquidshopper.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.liquidshopper.domain.models.Task

interface TasksRepository {

    fun tasksFlow(): Flow<List<Task>>

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

}