package ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel

import androidx.compose.runtime.Immutable
import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.domain.models.BackgroundImage

@Immutable
@Parcelize
data class TasksState(
    val tasks: List<GroupedTasksByDay> = emptyList(),
    val isNewCreateModalShow: Boolean = false,
    val emptyTitleError: Boolean = false,
    val taskType: TaskTypes = TaskTypes.OTHER,
    val selectedDay: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),
    val isShowSelectMonthDialog: Boolean = false,
    val isEditModalShow: Boolean = false,
    val editTaskId: Int? = null,
    val editType: TaskTypes = TaskTypes.OTHER,
    val editDateTime: LocalDateTime = LocalDateTime.now(),
    val emptyEditTitleError: Boolean = false,
    val background: BackgroundImage = BackgroundImage.BACKGROUND_PATTERN_1
) : Parcelable

sealed class TasksSideEffects {
    data class NavigateToDetails(val taskId: Int) : TasksSideEffects()
    data object NavigateToSettingsScreen : TasksSideEffects()
    data object NavigateToStatisticsScreen : TasksSideEffects()
}
