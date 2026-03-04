package ru.asmelnikov.liquidshopper.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsViewModel
import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState

@Composable
fun ItemsScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    taskId: Int,
    viewModel: ItemsViewModel = koinViewModel(parameters = {
        parametersOf(taskId)
    })
) {
    val state by viewModel.container.stateFlow.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red))

}