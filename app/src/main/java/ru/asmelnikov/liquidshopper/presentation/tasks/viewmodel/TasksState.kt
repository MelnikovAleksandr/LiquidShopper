package ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel

import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import java.time.LocalDate
import java.time.LocalTime

data class TasksState(
    val tasks: List<GroupedTasksByDay> = emptyList(),
    val title: String = "",
    val isModalShow: Boolean = false,
    val emptyTitleError: Boolean = false,
    val taskType: TaskTypes = TaskTypes.OTHER,
    val selectedDay: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),
    val isShowSelectMonthDialog: Boolean = false
)
