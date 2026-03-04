package ru.asmelnikov.liquidshopper.data.di

import androidx.room.Room
import org.koin.dsl.module
import ru.asmelnikov.liquidshopper.data.local.ShopperDatabase
import ru.asmelnikov.liquidshopper.data.repository.ItemsRepositoryImpl
import ru.asmelnikov.liquidshopper.data.repository.TasksRepositoryImpl
import ru.asmelnikov.liquidshopper.domain.repository.ItemsRepository
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository

private const val DB_NAME = "shopper_database"

val dataModule = module {

    single<ShopperDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = ShopperDatabase::class.java,
            name = DB_NAME
        ).build()
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

}