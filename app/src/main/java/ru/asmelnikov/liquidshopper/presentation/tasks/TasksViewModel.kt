package ru.asmelnikov.liquidshopper.presentation.tasks

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository
import java.time.LocalDateTime
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
                successCreate = false
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
                timeStamp = LocalDateTime.now(),
                items = emptyList()
            )
        )
        reduce { state.copy(successCreate = true) }
    }

    private fun subscribeTasks() = intent {
        tasksRepository.tasksFlow().collect { tasks ->
            reduce { state.copy(tasks = tasks) }
        }
    }

}