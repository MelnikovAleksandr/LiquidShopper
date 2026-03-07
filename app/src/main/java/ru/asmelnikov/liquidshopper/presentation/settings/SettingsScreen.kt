package ru.asmelnikov.liquidshopper.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState
import ru.asmelnikov.liquidshopper.presentation.navigation.popUp
import ru.asmelnikov.liquidshopper.presentation.settings.components.BackgroundItem
import ru.asmelnikov.liquidshopper.presentation.settings.components.Header
import ru.asmelnikov.liquidshopper.presentation.settings.viewmodel.SettingsSideEffects
import ru.asmelnikov.liquidshopper.presentation.settings.viewmodel.SettingsState
import ru.asmelnikov.liquidshopper.presentation.settings.viewmodel.SettingsViewModel
import java.util.UUID

@Composable
fun SettingsScreen(
    appState: MainAppState,
    viewModel: SettingsViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            SettingsSideEffects.NavigateBack -> appState.popUp()
        }
    }

    SettingsScreenContent(
        state = state,
        onBackClick = viewModel::navigateBack,
        onBackChange = viewModel::onBackChange
    )
}

@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onBackChange: (Background) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val liquidState = rememberLiquidState()
    val hazeState = rememberHazeState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(state.background.drawableRes),
            contentDescription = null,
            modifier = Modifier
                .liquefiable(liquidState)
                .hazeSource(state = hazeState)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .liquefiable(liquidState)
                .fillMaxSize(),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            stickyHeader {
                Header(
                    hazeState = hazeState,
                    liquidState = liquidState,
                    onBackClick = onBackClick
                )
            }
            items(items = state.backgrounds, key = { UUID.randomUUID() }) { item ->
                BackgroundItem(
                    modifier = Modifier
                        .animateItem()
                        .hazeSource(state = hazeState),
                    background = item,
                    onBackChange = onBackChange
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height((24 + 80).dp)
                        .navigationBarsPadding()
                )
            }
        }
    }
}