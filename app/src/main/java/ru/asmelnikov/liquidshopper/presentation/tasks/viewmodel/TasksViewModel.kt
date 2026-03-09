package ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.domain.models.Screens
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.domain.repository.ScreensBackgroundRepository
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository
import ru.asmelnikov.liquidshopper.utils.intent.sharedTask
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class TasksViewModel(
    private val tasksRepository: TasksRepository,
    private val screensBackgroundRepository: ScreensBackgroundRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<TasksState, TasksSideEffects> {

    override val container = container<TasksState, TasksSideEffects>(
        initialState = TasksState(),
        savedStateHandle = savedStateHandle
    )

    init {
        getScreenImage()
        subscribeTasks()
    }

    fun deleteTask(task: Task) = intent {
        tasksRepository.deleteTask(task)
    }

    fun onCreateClick() = intent {
        reduce { state.copy(isNewCreateModalShow = true) }
    }

    fun onModalDismiss() = intent {
        reduce {
            state.copy(
                isNewCreateModalShow = false,
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
            task = createNewTask(
                title = state.title,
                type = state.taskType,
                timeStamp = state.selectedDay.atTime(state.selectedTime)
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

    fun onEditClick(task: Task) = intent {
        reduce {
            state.copy(
                isEditModalShow = true,
                editTaskId = task.uid,
                editTitle = task.taskName,
                editType = task.taskType,
                editDateTime = task.timeStamp,
                emptyEditTitleError = false
            )
        }
    }

    fun onEditDismiss() = intent {
        reduce {
            state.copy(
                isEditModalShow = false,
                editTaskId = null,
                editTitle = "",
                editType = TaskTypes.OTHER,
                editDateTime = LocalDateTime.now(),
                emptyEditTitleError = false
            )
        }
    }

    fun onEditTitle(title: String) = intent {
        reduce {
            state.copy(
                editTitle = title,
                emptyEditTitleError = false
            )
        }
    }

    fun onEditType(type: TaskTypes) = intent {
        reduce {
            state.copy(
                editType = type
            )
        }
    }

    fun onEditTimeStamp(dateTime: LocalDateTime) = intent {
        reduce {
            state.copy(
                editDateTime = dateTime
            )
        }
    }

    fun onConfirmEditClick() = intent {
        if (state.editTitle.isBlank()) {
            reduce { state.copy(emptyEditTitleError = true) }
            return@intent
        }
        tasksRepository.updateTask(
            task = createUpdateTask(
                uid = state.editTaskId,
                title = state.editTitle,
                type = state.editType,
                timeStamp = state.editDateTime
            )
        )
        onEditDismiss()
    }

    fun onTaskShare(task: Task, context: Context) = intent {
        sharedTask(task = task, context = context)
    }

    fun navigateToDetails(taskId: Int) = intent {
        postSideEffect(TasksSideEffects.NavigateToDetails(taskId = taskId))
    }

    fun onSettingsClick() = intent {
        postSideEffect(TasksSideEffects.NavigateToSettingsScreen)
    }

    fun onStatisticsClick() = intent {
        postSideEffect(TasksSideEffects.NavigateToStatisticsScreen)
    }

    private fun subscribeTasks() = intent {
        tasksRepository.tasksFlow().collect { tasks ->
            reduce { state.copy(tasks = tasks) }
        }
    }

    private fun getScreenImage() = intent {
        screensBackgroundRepository.getCurrentScreenData(Screens.MAIN).collect { data ->
            reduce { state.copy(background = data.data) }
        }
    }

    private fun createNewTask(title: String, type: TaskTypes, timeStamp: LocalDateTime): Task {
        return Task(
            uid = UUID.randomUUID().hashCode(),
            taskName = title,
            taskType = type,
            isCompleted = false,
            allItemsCount = 0,
            inProgressItemsCount = 0,
            timeStamp = timeStamp,
            sumPrice = 0,
            items = emptyList()
        )
    }

    private fun createUpdateTask(uid: Int?, title: String, type: TaskTypes, timeStamp: LocalDateTime): Task {
        return Task(
            uid = uid ?: UUID.randomUUID().hashCode(),
            taskName = title,
            taskType = type,
            isCompleted = false,
            allItemsCount = 0,
            inProgressItemsCount = 0,
            timeStamp = timeStamp,
            sumPrice = 0,
            items = emptyList()
        )
    }

}