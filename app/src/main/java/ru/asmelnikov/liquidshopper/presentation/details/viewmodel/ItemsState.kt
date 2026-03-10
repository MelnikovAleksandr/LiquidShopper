package ru.asmelnikov.liquidshopper.presentation.details.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.liquidshopper.domain.models.BackgroundImage
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.UnitType
import ru.asmelnikov.liquidshopper.utils.locale.UiText

@Immutable
@Parcelize
data class ItemsState(
    val taskId: Int,
    val task: Task? = null,
    val isNewItemModalShow: Boolean = false,
    val newItemCount: Int = 1,
    val newItemUnit: UnitType = UnitType.PIECES,
    val newItemPrice: Int = 0,
    val emptyNewItemNameError: Boolean = false,
    val isEditModalShow: Boolean = false,
    val editItem: Item? = null,
    val emptyEditItemNameError: Boolean = false,
    val background: BackgroundImage = BackgroundImage.BACKGROUND_PATTERN_1
) : Parcelable

sealed class ItemsSideEffects {
    data object NavigateBack : ItemsSideEffects()
    data class DeleteSnackbarWithDismiss(val item: Item, val text: UiText.StringResource, val buttonText: UiText.StringResource) : ItemsSideEffects()
}
