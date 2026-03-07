package ru.asmelnikov.liquidshopper.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.liquidshopper.data.models.ScreensBackEntity

@Dao
interface ScreensImagesDao {

    @Query("SELECT * FROM ScreensBackEntity")
    fun getAllScreensBack(): Flow<List<ScreensBackEntity>>

    @Query("SELECT * FROM ScreensBackEntity WHERE screenId = :screenId")
    fun getScreensBackById(screenId: Int): Flow<ScreensBackEntity>

    @Update
    suspend fun updateScreenImage(data: ScreensBackEntity)

}