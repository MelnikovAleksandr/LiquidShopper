package ru.asmelnikov.liquidshopper.domain.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.liquidshopper.R

@Parcelize
data class Background(
    val screen: Screens,
    val data: BackgroundImage
) : Parcelable

enum class BackgroundImage(
    @DrawableRes val drawableRes: Int,
    val value: Int
) {
    BACKGROUND_PATTERN_1(R.drawable.background_pattern_1, 1),
    BACKGROUND_PATTERN_2(R.drawable.background_pattern_2, 2),
    BACKGROUND_PATTERN_3(R.drawable.background_pattern_3, 3),
    BACKGROUND_PATTERN_4(R.drawable.background_pattern_4, 4),
    BACKGROUND_PATTERN_5(R.drawable.background_pattern_5, 5),
    BACKGROUND_PATTERN_6(R.drawable.background_pattern_6, 6),
    BACKGROUND_PATTERN_7(R.drawable.background_pattern_7, 7),
    BACKGROUND_PATTERN_8(R.drawable.background_pattern_8, 8),
    BACKGROUND_PATTERN_9(R.drawable.background_pattern_9, 9),
    BACKGROUND_PATTERN_10(R.drawable.background_pattern_10, 10),
    BACKGROUND_PATTERN_11(R.drawable.background_pattern_11, 11),
    BACKGROUND_PATTERN_12(R.drawable.background_pattern_12, 12),
    BACKGROUND_PATTERN_13(R.drawable.background_pattern_13, 13);

    companion object {
        fun fromValue(value: Int): BackgroundImage =
            entries.find { it.value == value } ?: BACKGROUND_PATTERN_1
    }
}

enum class Screens(@StringRes val stringRes: Int, val value: Int) {
    MAIN(R.string.main_screen, 1),
    DETAILS(R.string.details_screen, 2),
    STATISTICS(R.string.statistics_screen, 3),
    SETTINGS(R.string.settings_screen, 4);

    companion object {
        fun fromValue(value: Int): Screens =
            Screens.entries.find { it.value == value } ?: MAIN
    }
}