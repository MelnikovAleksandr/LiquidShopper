package ru.asmelnikov.liquidshopper.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.asmelnikov.liquidshopper.data.local.TasksDao
import ru.asmelnikov.liquidshopper.data.models.toGroupedTasksByDay
import ru.asmelnikov.liquidshopper.data.models.toTaskEntity
import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository

class TasksRepositoryImpl(
    private val tasksDao: TasksDao
): TasksRepository {

    override fun tasksFlow(): Flow<List<GroupedTasksByDay>> {
        return tasksDao.getAllTasksWithItemsFlow().flowOn(Dispatchers.IO).map { it.toGroupedTasksByDay() }
    }

    override suspend fun insertTask(task: Task) {
        tasksDao.insertTask(task.toTaskEntity())
    }

    override suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task.toTaskEntity())
    }

    override suspend fun updateTask(task: Task) {
        tasksDao.updateTask(task.toTaskEntity())
    }
}