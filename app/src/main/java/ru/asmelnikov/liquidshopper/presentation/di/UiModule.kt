package ru.asmelnikov.liquidshopper.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksViewModel

val uiModule = module {

    viewModel {
        TasksViewModel(
            tasksRepository = get()
        )
    }

}