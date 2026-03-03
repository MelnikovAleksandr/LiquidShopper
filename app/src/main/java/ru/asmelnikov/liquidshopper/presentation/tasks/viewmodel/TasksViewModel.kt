package ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TasksViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel(), ContainerHost<TasksState, Nothing> {

    override val container = container<TasksState, Nothing>(
        initialState = TasksState()
    )

    init {
        subscribeTasks()
    }

    fun deleteTask(task: Task) = intent {
        tasksRepository.deleteTask(task)
    }

    fun onCreateClick() = intent {
        reduce { state.copy(isModalShow = true) }
    }

    fun onModalDismiss() = intent {
        reduce {
            state.copy(
                isModalShow = false,
                title = "",
                taskType = TaskTypes.OTHER,
                emptyTitleError = false,
                selectedTime = LocalTime.now()
            )
        }
    }

    fun onTitleChange(text: String) = intent {
        reduce { state.copy(title = text, emptyTitleError = false) }
    }

    fun onTaskTypeChange(taskType: TaskTypes) = intent {
        reduce { state.copy(taskType = taskType) }
    }

    fun onConfirmCreateClick() = intent {
        if (state.title.isBlank()) {
            reduce { state.copy(emptyTitleError = true) }
            return@intent
        }
        tasksRepository.insertTask(
            Task(
                uid = UUID.randomUUID().hashCode(),
                taskName = state.title,
                taskType = state.taskType,
                isCompleted = false,
                allItemsCount = 0,
                inProgressItemsCount = 0,
                timeStamp = state.selectedDay.atTime(state.selectedTime),
                items = emptyList()
            )
        )
        onModalDismiss()
    }

    fun onSelectedDayChange(day: LocalDate) = intent {
        reduce { state.copy(selectedDay = day) }
    }

    fun onTimeStampChange(time: LocalTime) = intent {
        reduce { state.copy(selectedTime = time) }
    }

    fun onMonthDialogShow() = intent {
        reduce { state.copy(isShowSelectMonthDialog = true) }
    }

    fun onDismissMonthDialog() = intent {
        reduce { state.copy(isShowSelectMonthDialog = false) }
    }

    private fun subscribeTasks() = intent {
        tasksRepository.tasksFlow().collect { tasks ->
            reduce { state.copy(tasks = tasks) }
        }
    }

}