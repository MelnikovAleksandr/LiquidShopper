package ru.asmelnikov.liquidshopper.utils.datetime

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import ru.asmelnikov.liquidshopper.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val FULL_DATE_TIME = "dd.MM.yy HH:mm"
private const val TITLE_DATE = "d MMMM"

fun LocalDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern(FULL_DATE_TIME)
    return this.format(formatter)
}

fun LocalDate.toDayMonthString(): String {
    val formatter = DateTimeFormatter.ofPattern(TITLE_DATE, Locale.forLanguageTag("ru"))
    return this.format(formatter).replaceFirstChar { it.uppercase() }
}

@Composable
fun YearMonth.toDisplay(): String {
    val monthValue = this.monthValue - 1
    val year = this.year
    val months = stringArrayResource(id = R.array.months).toList()
    val monthResult = months[monthValue]
    return "$monthResult, $year"
}