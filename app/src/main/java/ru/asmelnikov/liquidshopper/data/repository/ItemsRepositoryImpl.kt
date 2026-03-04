package ru.asmelnikov.liquidshopper.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.asmelnikov.liquidshopper.data.local.ItemsDao
import ru.asmelnikov.liquidshopper.data.models.toTaskItemEntity
import ru.asmelnikov.liquidshopper.data.models.toTasks
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.repository.ItemsRepository

class ItemsRepositoryImpl(
    private val itemsDao: ItemsDao
): ItemsRepository {
    override fun taskFlow(taskId: Int): Flow<Task> {
        return itemsDao.getTaskWithItemsByTaskId(taskId = taskId).map { it.toTasks() }
    }

    override suspend fun insertItem(item: Item) {
        itemsDao.insertTaskItem(taskItem = item.toTaskItemEntity())
    }

    override suspend fun deleteItem(item: Item) {
        itemsDao.deleteTaskItem(taskItem = item.toTaskItemEntity())
    }

    override suspend fun updateItem(item: Item) {
        itemsDao.updateTaskItem(taskItem = item.toTaskItemEntity())
    }
}