package ru.asmelnikov.liquidshopper.presentation.statistics.components.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.presentation.statistics.utils.toPeriodString
import ru.asmelnikov.liquidshopper.presentation.statistics.viewmodel.StatisticsState
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.datetime.toDisplay
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun PeriodPickerDialog(
    modifier: Modifier = Modifier,
    state: StatisticsState,
    onDismiss: () -> Unit,
    onConfirmChosenPeriod: (LocalDate, LocalDate) -> Unit
) {
    if (state.isCosePeriodDialogShow) {
        val context = LocalContext.current
        var chosenStartDate by rememberSaveable {
            mutableStateOf<LocalDate?>(state.selectedStart)
        }
        var chosenEndDate by rememberSaveable {
            mutableStateOf<LocalDate?>(state.selectedEnd)
        }

        val currentMonth = remember { YearMonth.now() }
        val startMonth = remember { currentMonth.minusMonths(300) }
        val endMonth = remember { currentMonth.plusMonths(300) }
        val today = remember { LocalDate.now() }
        val daysOfWeek = remember { daysOfWeek() }

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = modifier
                    .fillMaxHeight(0.7f)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val calendarState = rememberCalendarState(
                    startMonth = startMonth,
                    endMonth = endMonth,
                    firstVisibleMonth = currentMonth,
                    firstDayOfWeek = daysOfWeek.first(),
                )

                Column(modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = Modifier
                                .basicMarquee(Int.MAX_VALUE)
                                .weight(1f)
                                .padding(horizontal = dimens.small3),
                            text = (chosenStartDate to chosenEndDate).toPeriodString(context = context),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                        TextButton(
                            onClick = {
                                chosenStartDate = null
                                chosenEndDate = null
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.stats_clear),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimens.extraSmall1),
                    ) {
                        for (dayOfWeek in daysOfWeek) {
                            Text(
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.secondary,
                                text = dayOfWeek.displayText(),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    HorizontalDivider()
                }


                VerticalCalendar(
                    modifier = Modifier.weight(1f),
                    state = calendarState,
                    contentPadding = PaddingValues(bottom = dimens.large),
                    dayContent = { value ->
                        Day(
                            value,
                            today = today,
                            startDate = chosenStartDate,
                            endDate = chosenEndDate
                        ) { day ->
                            if (day.position == DayPosition.MonthDate) {
                                chosenStartDate = getSelection(
                                    clickedDate = day.date,
                                    startDate = chosenStartDate,
                                    endDate = chosenEndDate
                                ).first
                                chosenEndDate = getSelection(
                                    clickedDate = day.date,
                                    startDate = chosenStartDate,
                                    endDate = chosenEndDate
                                ).second
                            }
                        }
                    },
                    monthHeader = { month -> MonthHeader(month) },
                )
                Column(modifier.fillMaxWidth()) {
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        TextButton(
                            onClick = onDismiss
                        ) {
                            Text(
                                text = stringResource(R.string.stats_dismiss),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1
                            )
                        }

                        TextButton(
                            enabled = checkDaysBetween(chosenStartDate, chosenEndDate) != null,
                            colors = ButtonDefaults.textButtonColors(
                                disabledContentColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            onClick = {
                                val startDate = chosenStartDate
                                val endDate = chosenEndDate
                                if (startDate != null && endDate != null) {
                                    onConfirmChosenPeriod(startDate, endDate)
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.stats_confirm),
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimens.small3,
                bottom = dimens.small1,
                start = dimens.medium1,
                end = dimens.medium1
            ),
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = calendarMonth.yearMonth.toDisplay(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    today: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onClick: (CalendarDay) -> Unit,
) {
    var textColor = Color.Transparent
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                showRipple = false,
                onClick = { onClick(day) },
            )
            .backgroundHighlight(
                day = day,
                today = today,
                startDate = startDate,
                endDate = endDate,
                selectionColor = MaterialTheme.colorScheme.onBackground,
                continuousSelectionColor = MaterialTheme.colorScheme.secondary,
            ) { textColor = it },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Preview
@Composable
private fun PeriodPickerDialogPreview() {
    LiquidShopperTheme {
        PeriodPickerDialog(
            state = StatisticsState(),
            onDismiss = {},
            onConfirmChosenPeriod = { _, _ -> }
        )
    }
}