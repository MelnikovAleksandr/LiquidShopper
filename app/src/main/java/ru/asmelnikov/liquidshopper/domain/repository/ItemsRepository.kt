package ru.asmelnikov.liquidshopper.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Task

interface ItemsRepository {

    fun taskFlow(taskId: Int): Flow<Task>

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)

}