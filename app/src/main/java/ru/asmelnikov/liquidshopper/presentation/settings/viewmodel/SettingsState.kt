package ru.asmelnikov.liquidshopper.presentation.settings.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.domain.models.BackgroundImage

@Immutable
@Parcelize
data class SettingsState(
    val backgrounds: List<Background> = emptyList(),
    val background: BackgroundImage = BackgroundImage.BACKGROUND_PATTERN_1
): Parcelable

sealed class SettingsSideEffects {
    data object NavigateBack : SettingsSideEffects()
}
