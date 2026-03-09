package ru.asmelnikov.liquidshopper.presentation.statistics.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.liquidshopper.domain.models.BackgroundImage
import ru.asmelnikov.liquidshopper.domain.models.Statistic
import java.time.LocalDate

@Immutable
@Parcelize
data class StatisticsState(
    val statistic: Statistic? = null,
    val background: BackgroundImage = BackgroundImage.BACKGROUND_PATTERN_1,
    val selectedStart: LocalDate = LocalDate.now().withDayOfMonth(1),
    val selectedEnd: LocalDate = LocalDate.now(),
    val isCosePeriodDialogShow: Boolean = false
) : Parcelable {
    fun isEmptyPeriodData(): Boolean =
        (statistic?.dataMapSet?.count() ?: 0) < 2 && (statistic?.itemsByDay?.isEmpty() == true)
}


sealed class StatisticsSideEffects {
    data object NavigateBack : StatisticsSideEffects()
}
