package ru.asmelnikov.liquidshopper.presentation.details.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.UnitType

@Immutable
@Parcelize
data class ItemsState(
    val taskId: Int,
    val task: Task? = null,
    val isNewItemModalShow: Boolean = false,
    val newItemName: String = "",
    val newItemCount: Int = 1,
    val newItemUnit: UnitType = UnitType.PIECES,
    val newItemPrice: Int = 0,
    val emptyNewItemNameError: Boolean = false,
    val isEditModalShow: Boolean = false,
    val editItem: Item? = null,
    val emptyEditItemNameError: Boolean = false
) : Parcelable

sealed class ItemsSideEffects {
    data object NavigateBack : ItemsSideEffects()
}
