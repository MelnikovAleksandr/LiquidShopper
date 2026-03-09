package ru.asmelnikov.liquidshopper.presentation.statistics.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.domain.models.Screens
import ru.asmelnikov.liquidshopper.domain.repository.ScreensBackgroundRepository
import ru.asmelnikov.liquidshopper.domain.repository.StatisticsRepository
import java.time.LocalDate

class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
    private val screensBackgroundRepository: ScreensBackgroundRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<StatisticsState, StatisticsSideEffects> {

    override val container = container<StatisticsState, StatisticsSideEffects>(
        initialState = StatisticsState(),
        savedStateHandle = savedStateHandle
    )

    init {
        getScreenImage()
        getStatistics()
    }

    fun navigateBack() = intent {
        postSideEffect(StatisticsSideEffects.NavigateBack)
    }

    fun onChosePeriodClick() = intent {
        reduce { state.copy(isCosePeriodDialogShow = true) }
    }

    fun onChosePeriodDismiss() = intent {
        reduce { state.copy(isCosePeriodDialogShow = false) }
    }

    fun onSelectDateConfirm(
        selectedStart: LocalDate, selectedEnd: LocalDate
    ) = intent {
        reduce {
            state.copy(
                isCosePeriodDialogShow = false,
                selectedStart = selectedStart,
                selectedEnd = selectedEnd
            )
        }
        getStatistics()
    }

    private fun getStatistics() = intent {
        val statistic = statisticsRepository.getPeriodStatistics(
            start = state.selectedStart,
            end = state.selectedEnd
        )
        reduce { state.copy(statistic = statistic) }
    }

    private fun getScreenImage() = intent {
        screensBackgroundRepository.getCurrentScreenData(Screens.DETAILS).collect { data ->
            reduce { state.copy(background = data.data) }
        }
    }
}