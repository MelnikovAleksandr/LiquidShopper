package ru.asmelnikov.liquidshopper.presentation.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import ru.asmelnikov.liquidshopper.presentation.details.ItemsScreen
import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState
import ru.asmelnikov.liquidshopper.presentation.tasks.TasksScreen

@Composable
fun SharedTransitionScope.NavGraph(
    appState: MainAppState,
    paddingValues: PaddingValues,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit
) {
    NavDisplay(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        backStack = appState.backStack,
        entryProvider = entryProvider {
            entry<Routes.TasksScreen> {
                TasksScreen(
                    appState = appState,
                    showSnackbar = showSnackbar,
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                )
            }

            entry<Routes.ItemsScreen> {
                ItemsScreen(
                    appState = appState,
                    showSnackbar = showSnackbar,
                    taskId = it.taskId,
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                )
            }
        },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
    )
}