package ru.asmelnikov.liquidshopper.data.di

import androidx.room.Room
import org.koin.dsl.module
import ru.asmelnikov.liquidshopper.data.local.ShopperDatabase
import ru.asmelnikov.liquidshopper.data.repository.ItemsRepositoryImpl
import ru.asmelnikov.liquidshopper.data.repository.ScreensBackgroundRepositoryImpl
import ru.asmelnikov.liquidshopper.data.repository.TasksRepositoryImpl
import ru.asmelnikov.liquidshopper.domain.repository.ItemsRepository
import ru.asmelnikov.liquidshopper.domain.repository.ScreensBackgroundRepository
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository

private const val DB_NAME = "shopper_database"
private const val DB_ASSETS = "shopper_screens.db"

val dataModule = module {

    single<ShopperDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = ShopperDatabase::class.java,
            name = DB_NAME
        ).createFromAsset(DB_ASSETS).build()
    }

    single<TasksRepository> {
        TasksRepositoryImpl(
            tasksDao = get<ShopperDatabase>().getTasksDao()
        )
    }

    single<ItemsRepository> {
        ItemsRepositoryImpl(
            itemsDao = get<ShopperDatabase>().getItemsDao()
        )
    }

    single<ScreensBackgroundRepository> {
        ScreensBackgroundRepositoryImpl(
            screensDao = get<ShopperDatabase>().getScreensDao()
        )
    }

}