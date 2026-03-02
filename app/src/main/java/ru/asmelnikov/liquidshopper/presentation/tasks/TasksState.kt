package ru.asmelnikov.liquidshopper.presentation.tasks

import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes

data class TasksState(
    val tasks: List<Task> = emptyList(),
    val title: String = "",
    val isModalShow: Boolean = false,
    val emptyTitleError: Boolean = false,
    val successCreate: Boolean = false,
    val taskType: TaskTypes = TaskTypes.OTHER
)
