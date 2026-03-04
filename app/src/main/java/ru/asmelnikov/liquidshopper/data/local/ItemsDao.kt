package ru.asmelnikov.liquidshopper.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.liquidshopper.data.models.TaskItemEntity
import ru.asmelnikov.liquidshopper.data.models.TaskWithItems

@Dao
interface ItemsDao {

    @Transaction
    @Query("SELECT * FROM taskentity WHERE uid = :taskId")
    fun getTaskWithItemsByTaskId(taskId: Int): Flow<TaskWithItems>

    @Insert
    suspend fun insertTaskItem(taskItem: TaskItemEntity)

    @Update
    suspend fun updateTaskItem(taskItem: TaskItemEntity)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItemEntity)

}