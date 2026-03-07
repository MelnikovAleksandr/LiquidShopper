package ru.asmelnikov.liquidshopper.presentation.settings.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.domain.models.Screens
import ru.asmelnikov.liquidshopper.domain.repository.ScreensBackgroundRepository

class SettingsViewModel(
    private val screensBackgroundRepository: ScreensBackgroundRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<SettingsState, SettingsSideEffects> {

    override val container = container<SettingsState, SettingsSideEffects>(
        initialState = SettingsState(),
        savedStateHandle = savedStateHandle
    )

    init {
        getScreenImage()
        subscribeScreens()
    }

    fun onBackChange(background: Background) = intent {
        screensBackgroundRepository.updateScreenData(background)
    }

    fun navigateBack() = intent {
        postSideEffect(SettingsSideEffects.NavigateBack)
    }

    private fun subscribeScreens() = intent {
        screensBackgroundRepository.getAllScreensData().collect { data ->
            reduce { state.copy(backgrounds = data) }
        }
    }

    private fun getScreenImage() = intent {
        screensBackgroundRepository.getCurrentScreenData(Screens.SETTINGS).collect { data ->
            reduce { state.copy(background = data.data) }
        }
    }

}