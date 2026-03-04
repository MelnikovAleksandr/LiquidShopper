package ru.asmelnikov.liquidshopper.presentation.details.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.liquidshopper.domain.models.Task

@Immutable
@Parcelize
data class ItemsState(
    val taskId: Int,
    val task: Task? = null
) : Parcelable

sealed class ItemsSideEffects {
    data object NavigateBack: ItemsSideEffects()
}
