package ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var title by mutableStateOf("")
        private set

    var editTitle by mutableStateOf("")
        private set

    fun deleteTask(task: Task) = intent {
        tasksRepository.deleteTask(task)
    }

    fun onCreateClick() = intent {
        reduce { state.copy(isNewCreateModalShow = true) }
    }

    fun onModalDismiss() {
        title = ""
        intent {
            reduce {
                state.copy(
                    isNewCreateModalShow = false,
                    taskType = TaskTypes.OTHER,
                    emptyTitleError = false,
                    selectedTime = LocalTime.now()
                )
            }
        }
    }

    fun onTitleChange(text: String) {
        title = text
        intent {
            reduce { state.copy(emptyTitleError = false) }
        }
    }

    fun onTaskTypeChange(taskType: TaskTypes) = intent {
        reduce { state.copy(taskType = taskType) }
    }

    fun onConfirmCreateClick() = intent {
        if (title.isBlank()) {
            reduce { state.copy(emptyTitleError = true) }
            return@intent
        }
        tasksRepository.insertTask(
            task = createNewTask(
                title = title,
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

    fun onEditClick(task: Task) {
        editTitle = task.taskName
        intent {
            reduce {
                state.copy(
                    isEditModalShow = true,
                    editTaskId = task.uid,
                    editType = task.taskType,
                    editDateTime = task.timeStamp,
                    emptyEditTitleError = false
                )
            }
        }
    }

    fun onEditDismiss() {
        editTitle = ""
        intent {
            reduce {
                state.copy(
                    isEditModalShow = false,
                    editTaskId = null,
                    editType = TaskTypes.OTHER,
                    editDateTime = LocalDateTime.now(),
                    emptyEditTitleError = false
                )
            }
        }
    }

    fun onEditTitle(title: String) {
        editTitle = title
        intent {
            reduce {
                state.copy(
                    emptyEditTitleError = false
                )
            }
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
        if (editTitle.isBlank()) {
            reduce { state.copy(emptyEditTitleError = true) }
            return@intent
        }
        tasksRepository.updateTask(
            task = createUpdateTask(
                uid = state.editTaskId,
                title = editTitle,
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

    private fun createUpdateTask(
        uid: Int?,
        title: String,
        type: TaskTypes,
        timeStamp: LocalDateTime
    ): Task {
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