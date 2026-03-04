package ru.asmelnikov.liquidshopper.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.liquidshopper.data.models.TaskEntity
import ru.asmelnikov.liquidshopper.data.models.TaskWithItems

@Dao
interface TasksDao {

    @Transaction
    @Query("SELECT * FROM taskentity ORDER BY timeStamp DESC")
    fun getAllTasksWithItemsFlow(): Flow<List<TaskWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

}