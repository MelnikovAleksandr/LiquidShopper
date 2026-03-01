package ru.asmelnikov.liquidshopper.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.asmelnikov.liquidshopper.data.local.TasksDao
import ru.asmelnikov.liquidshopper.data.models.toTaskEntity
import ru.asmelnikov.liquidshopper.data.models.toTasks
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository

class TasksRepositoryImpl(
    private val tasksDao: TasksDao
): TasksRepository {

    override fun tasksFlow(): Flow<List<Task>> {
        return tasksDao.getAllTasksWithItemsFlow().map { it.map { task -> task.toTasks() } }
    }

    override suspend fun insertTask(task: Task) {
        tasksDao.insertTask(task.toTaskEntity())
    }

    override suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task.toTaskEntity())
    }
}