package ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel

import androidx.compose.runtime.Immutable
import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Immutable
data class TasksState(
    val tasks: List<GroupedTasksByDay> = emptyList(),
    val title: String = "",
    val isNewCreateModalShow: Boolean = false,
    val emptyTitleError: Boolean = false,
    val taskType: TaskTypes = TaskTypes.OTHER,
    val selectedDay: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),
    val isShowSelectMonthDialog: Boolean = false,
    val isEditModalShow: Boolean = false,
    val editTaskId: Int? = null,
    val editTitle: String = "",
    val editType: TaskTypes = TaskTypes.OTHER,
    val editDateTime: LocalDateTime = LocalDateTime.now(),
    val emptyEditTitleError: Boolean = false
)
