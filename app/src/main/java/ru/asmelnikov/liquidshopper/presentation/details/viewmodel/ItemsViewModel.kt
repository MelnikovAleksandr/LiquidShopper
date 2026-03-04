package ru.asmelnikov.liquidshopper.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.domain.repository.ItemsRepository

class ItemsViewModel(
    private val itemsRepository: ItemsRepository,
    taskId: Int,
    savedStateHandle: SavedStateHandle
): ViewModel(), ContainerHost<ItemsState, Nothing> {

    override val container = container<ItemsState, Nothing>(
        initialState = ItemsState(taskId = taskId),
        savedStateHandle = savedStateHandle
    )

    init {
        subscribeTask()
    }

    private fun subscribeTask() = intent {
        itemsRepository.taskFlow(state.taskId).collect { task ->
            reduce { state.copy(task = task) }
        }
    }

}