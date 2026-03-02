package ru.asmelnikov.liquidshopper.presentation.tasks.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector
import ru.asmelnikov.liquidshopper.R

enum class TaskDropDown(@StringRes val strRes: Int, val image: ImageVector) {
    EDIT(R.string.edit_task_drop_down, Icons.Filled.Edit),
    DELETE(R.string.delete_task_drop_down, Icons.Filled.Delete)
}