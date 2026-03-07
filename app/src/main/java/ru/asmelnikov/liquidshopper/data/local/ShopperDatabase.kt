package ru.asmelnikov.liquidshopper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.asmelnikov.liquidshopper.data.models.ScreensBackEntity
import ru.asmelnikov.liquidshopper.data.models.TaskEntity
import ru.asmelnikov.liquidshopper.data.models.TaskItemEntity


@Database(
    entities = [TaskEntity::class, TaskItemEntity::class, ScreensBackEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ShopperDatabase : RoomDatabase() {

    abstract fun getTasksDao(): TasksDao

    abstract fun getItemsDao(): ItemsDao

    abstract fun getScreensDao(): ScreensImagesDao

}