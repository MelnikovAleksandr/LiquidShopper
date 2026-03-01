package ru.asmelnikov.liquidshopper.presentation.tasks

import ru.asmelnikov.liquidshopper.domain.models.Task

data class TasksState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList()
)
