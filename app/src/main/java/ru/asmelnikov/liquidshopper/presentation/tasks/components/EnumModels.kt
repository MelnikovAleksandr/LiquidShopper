package ru.asmelnikov.liquidshopper.presentation.tasks.components

import androidx.annotation.StringRes
import ru.asmelnikov.liquidshopper.R

enum class TaskDropDown(@StringRes val strRes: Int) {
    EDIT(R.string.edit_task_drop_down),
    DELETE(R.string.delete_task_drop_down)
}