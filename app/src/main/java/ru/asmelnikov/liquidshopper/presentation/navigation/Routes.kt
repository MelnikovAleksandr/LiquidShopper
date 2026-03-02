package ru.asmelnikov.liquidshopper.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {
    @Serializable
    data object TasksScreen : Routes

    @Serializable
    data class ItemsScreen(val taskId: Int) : Routes
}