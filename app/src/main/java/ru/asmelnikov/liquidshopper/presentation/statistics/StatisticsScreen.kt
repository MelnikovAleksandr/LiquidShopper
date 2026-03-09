package ru.asmelnikov.liquidshopper.presentation.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.fastMap
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.dautovicharis.charts.PieChart
import io.github.dautovicharis.charts.model.toChartDataSet
import io.github.dautovicharis.charts.style.PieChartDefaults
import io.github.dautovicharis.charts.style.PieChartStyle
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState
import ru.asmelnikov.liquidshopper.presentation.navigation.popUp
import ru.asmelnikov.liquidshopper.presentation.statistics.components.FlowRowItems
import ru.asmelnikov.liquidshopper.presentation.statistics.components.Header
import ru.asmelnikov.liquidshopper.presentation.statistics.components.ItemView
import ru.asmelnikov.liquidshopper.presentation.statistics.components.picker.PeriodPickerDialog
import ru.asmelnikov.liquidshopper.presentation.statistics.viewmodel.StatisticsSideEffects
import ru.asmelnikov.liquidshopper.presentation.statistics.viewmodel.StatisticsState
import ru.asmelnikov.liquidshopper.presentation.statistics.viewmodel.StatisticsViewModel
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.datetime.toDayMonthString
import java.time.LocalDate

@Composable
fun StatisticsScreen(
    appState: MainAppState,
    viewModel: StatisticsViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            StatisticsSideEffects.NavigateBack -> appState.popUp()
        }
    }

    StatisticsScreenContent(
        state = state,
        onBackClick = viewModel::navigateBack,
        onChoseDateClick = viewModel::onChosePeriodClick,
        onDismissChosePeriod = viewModel::onChosePeriodDismiss,
        onConfirmChosenPeriod = viewModel::onSelectDateConfirm
    )

}

@Composable
fun StatisticsScreenContent(
    state: StatisticsState,
    onBackClick: () -> Unit,
    onChoseDateClick: () -> Unit,
    onDismissChosePeriod: () -> Unit,
    onConfirmChosenPeriod: (LocalDate, LocalDate) -> Unit
) {

    val liquidState = rememberLiquidState()
    val hazeState = rememberHazeState()

    PeriodPickerDialog(
        state = state,
        onDismiss = onDismissChosePeriod,
        onConfirmChosenPeriod = onConfirmChosenPeriod
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
            verticalArrangement = Arrangement.spacedBy(dimens.medium1)
        ) {
            stickyHeader {
                Header(
                    state = state,
                    liquidState = liquidState,
                    hazeState = hazeState,
                    onBackClick = onBackClick,
                    onChoseDateClick = onChoseDateClick
                )
            }

            if (state.isEmptyPeriodData()) {
                item {
                    Box(
                        modifier = Modifier
                            .hazeEffect(state = hazeState)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(dimens.small3),
                            text = stringResource(R.string.stats_empty),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                item {
                    val dataSet = (state.statistic?.dataMapSet?.values?.toMutableList()
                        ?: emptyList()).toChartDataSet(
                        title = stringResource(R.string.pie_title),
                        labels = (state.statistic?.dataMapSet?.keys?.toMutableList()
                            ?: emptyList()).fastMap {
                            stringResource(it.stringRes)
                        }
                    )

                    val customStyle = PieChartDefaults.style(
                        donutPercentage = 40f,
                        pieColors = (state.statistic?.dataMapSet?.keys?.toMutableList()
                            ?: emptyList()).fastMap {
                            it.color
                        },
                        legendVisible = false
                    )

                    val style = PieChartStyle(
                        modifier = Modifier
                            .padding(dimens.medium1)
                            .wrapContentSize()
                            .aspectRatio(1f)
                            .hazeSource(state = hazeState),
                        chartViewStyle = customStyle.chartViewStyle,
                        donutPercentage = customStyle.donutPercentage,
                        pieColors = customStyle.pieColors,
                        pieColor = customStyle.pieColor,
                        pieAlpha = customStyle.pieAlpha,
                        borderColor = customStyle.borderColor,
                        borderWidth = customStyle.borderWidth,
                        legendVisible = customStyle.legendVisible
                    )
                    if (dataSet.data.item.data.count() >= 2) {
                        PieChart(
                            dataSet = dataSet,
                            style = style
                        )
                    }
                }
                item {
                    AnimatedVisibility(visible = state.statistic != null) {
                        state.statistic?.let {
                            FlowRowItems(
                                items = state.statistic
                            )
                        }
                    }
                }

                state.statistic?.itemsByDay?.forEach { itemByDay ->

                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .hazeEffect(state = hazeState)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                                .statusBarsPadding()
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    vertical = dimens.small1,
                                    horizontal = dimens.medium1
                                ),
                                text = itemByDay.day.toDayMonthString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    items(items = itemByDay.items, key = { it.uid }) { item ->
                        ItemView(
                            modifier = Modifier
                                .animateItem()
                                .hazeSource(state = hazeState),
                            item = item,
                            liquidState = liquidState
                        )
                    }
                }

                item {
                    Spacer(
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            }
        }
    }
}
