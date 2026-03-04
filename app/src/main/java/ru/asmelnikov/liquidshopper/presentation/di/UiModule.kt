package ru.asmelnikov.liquidshopper.presentation.di

import androidx.lifecycle.SavedStateHandle
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsViewModel
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksViewModel

val uiModule = module {

    viewModel { (savedStateHandle: SavedStateHandle) ->
        TasksViewModel(
            tasksRepository = get(),
            savedStateHandle = savedStateHandle
        )
    }

    viewModel { (taskId: Int, savedStateHandle: SavedStateHandle) ->
        ItemsViewModel(
            itemsRepository = get(),
            taskId = taskId,
            savedStateHandle = savedStateHandle
        )
    }

}