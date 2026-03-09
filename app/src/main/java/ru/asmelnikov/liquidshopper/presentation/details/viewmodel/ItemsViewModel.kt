package ru.asmelnikov.liquidshopper.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Screens
import ru.asmelnikov.liquidshopper.domain.models.UnitType
import ru.asmelnikov.liquidshopper.domain.repository.ItemsRepository
import ru.asmelnikov.liquidshopper.domain.repository.ScreensBackgroundRepository
import ru.asmelnikov.liquidshopper.utils.locale.UiText
import java.time.LocalDateTime
import java.util.UUID

class ItemsViewModel(
    private val itemsRepository: ItemsRepository,
    private val screensBackgroundRepository: ScreensBackgroundRepository,
    taskId: Int,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ItemsState, ItemsSideEffects> {

    override val container = container<ItemsState, ItemsSideEffects>(
        initialState = ItemsState(taskId = taskId),
        savedStateHandle = savedStateHandle
    )

    init {
        getScreenImage()
        subscribeTask()
    }

    fun navigateBack() = intent {
        postSideEffect(ItemsSideEffects.NavigateBack)
    }

    fun onNewItemCreateClick() = intent {
        reduce { state.copy(isNewItemModalShow = true, emptyNewItemNameError = false) }
    }

    fun onNewItemDismiss() = intent {
        reduce {
            state.copy(
                isNewItemModalShow = false,
                emptyNewItemNameError = false,
                newItemName = "",
                newItemCount = 1,
                newItemUnit = UnitType.PIECES,
                newItemPrice = 0
            )
        }
    }

    fun onNewItemNameChange(title: String) = intent {
        reduce { state.copy(newItemName = title, emptyNewItemNameError = false) }
    }

    fun onNewItemCountChange(count: Int) = intent {
        reduce { state.copy(newItemCount = count) }
    }

    fun onNewItemUnitChange(unit: UnitType) = intent {
        reduce { state.copy(newItemUnit = unit) }
    }

    fun onNewItemPriceChange(price: Int) = intent {
        reduce { state.copy(newItemPrice = price) }
    }

    fun onNewCreateConfirm() = intent {
        if (state.newItemName.isBlank()) {
            reduce { state.copy(emptyNewItemNameError = true) }
            return@intent
        }
        itemsRepository.insertItem(
            createNewItem(state)
        )
        onNewItemDismiss()
    }

    fun recreateItem(item: Item) = intent {
        itemsRepository.insertItem(item)
    }

    fun onItemDelete(item: Item) = intent {
        itemsRepository.deleteItem(item)
        postSideEffect(
            ItemsSideEffects.DeleteSnackbarWithDismiss(
                item = item,
                text = UiText.StringResource(
                    resId = R.string.items_delete, item.itemName
                ),
                buttonText = UiText.StringResource(
                    resId = R.string.items_delete_recall
                )
            )
        )
    }

    fun onItemBoughtChange(item: Item) = intent {
        itemsRepository.updateItem(item)
    }

    fun onEditModalShow(item: Item) = intent {
        reduce {
            state.copy(
                editItem = item, isEditModalShow = true, emptyEditItemNameError = false
            )
        }
    }

    fun onEditModalDismiss() = intent {
        reduce {
            state.copy(
                editItem = null, isEditModalShow = false, emptyEditItemNameError = false
            )
        }
    }

    fun onEditConfirm(item: Item) = intent {
        if (item.itemName.isBlank()) {
            reduce { state.copy(emptyEditItemNameError = true) }
            return@intent
        }
        itemsRepository.updateItem(item)
        onEditModalDismiss()
    }

    fun onItemsChangeStatusCall() = intent {
        itemsRepository.allItemsStatusInverse(state.taskId, state.task?.isCompleted ?: false)
    }

    private fun subscribeTask() = intent {
        itemsRepository.taskFlow(state.taskId).collect { task ->
            reduce { state.copy(task = task) }
            if (task.items.isEmpty()) {
                onNewItemCreateClick()
            }
        }
    }

    private fun getScreenImage() = intent {
        screensBackgroundRepository.getCurrentScreenData(Screens.DETAILS).collect { data ->
            reduce { state.copy(background = data.data) }
        }
    }

    private fun createNewItem(state: ItemsState): Item {
        return Item(
            uid = UUID.randomUUID().hashCode(),
            timeStamp = state.task?.timeStamp ?: LocalDateTime.now(),
            taskId = state.taskId,
            itemName = state.newItemName,
            count = state.newItemCount,
            price = state.newItemPrice,
            units = state.newItemUnit,
            bought = false
        )
    }

}