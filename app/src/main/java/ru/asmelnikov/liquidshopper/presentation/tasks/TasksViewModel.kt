package ru.asmelnikov.liquidshopper.presentation.tasks

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.repository.TasksRepository

class TasksViewModel(
    private val tasksRepository: TasksRepository
): ViewModel(), ContainerHost<TasksState, Nothing> {

    override val container = container<TasksState, Nothing>(
        initialState = TasksState()
    )

    init {
        subscribeTasks()
    }

    fun insertTask(task: Task) = intent {
        tasksRepository.insertTask(task)
    }

    fun deleteTask(task: Task) = intent {
        tasksRepository.deleteTask(task)
    }

    private fun subscribeTasks() = intent {
        tasksRepository.tasksFlow().collect { tasks ->
            reduce { state.copy(tasks = tasks) }
        }
    }

}