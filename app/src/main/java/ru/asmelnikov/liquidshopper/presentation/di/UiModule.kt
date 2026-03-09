package ru.asmelnikov.liquidshopper.presentation.di

import androidx.lifecycle.SavedStateHandle
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsViewModel
import ru.asmelnikov.liquidshopper.presentation.settings.viewmodel.SettingsViewModel
import ru.asmelnikov.liquidshopper.presentation.statistics.viewmodel.StatisticsViewModel
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksViewModel

val uiModule = module {

    viewModel { (savedStateHandle: SavedStateHandle) ->
        TasksViewModel(
            tasksRepository = get(),
            screensBackgroundRepository = get(),
            savedStateHandle = savedStateHandle
        )
    }

    viewModel { (taskId: Int, savedStateHandle: SavedStateHandle) ->
        ItemsViewModel(
            itemsRepository = get(),
            screensBackgroundRepository = get(),
            taskId = taskId,
            savedStateHandle = savedStateHandle
        )
    }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        SettingsViewModel(
            screensBackgroundRepository = get(),
            savedStateHandle = savedStateHandle
        )
    }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        StatisticsViewModel(
            statisticsRepository = get(),
            screensBackgroundRepository = get(),
            savedStateHandle = savedStateHandle
        )
    }

}