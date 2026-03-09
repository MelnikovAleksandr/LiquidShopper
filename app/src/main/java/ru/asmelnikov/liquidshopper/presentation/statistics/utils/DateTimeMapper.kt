package ru.asmelnikov.liquidshopper.presentation.statistics.utils

import android.content.Context
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.utils.datetime.PERIOD
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Pair<LocalDate?, LocalDate?>.toPeriodString(context: Context): String {
    val today = LocalDate.now()
    val start = this.first ?: return ""
    val end = this.second ?: return ""

    val monthsFull = context.resources.getStringArray(R.array.months)
    val monthsShort = context.resources.getStringArray(R.array.months_short)

    val isCurrentMonthPartial = start.dayOfMonth == 1 &&
            end == today &&
            start.month == today.month &&
            start.year == today.year

    val isFullMonth = start.dayOfMonth == 1 &&
            end.dayOfMonth == end.lengthOfMonth() &&
            start.month == end.month

    if (isCurrentMonthPartial || isFullMonth) {
        val currentYear = LocalDate.now().year
        return if (start.year == currentYear) {
            monthsFull[start.monthValue - 1]
        } else {
            "${monthsFull[start.monthValue - 1]} ${start.year}"
        }
    }

    return if (start.year == today.year && end.year == today.year) {
        val startStr = "${start.dayOfMonth} ${monthsShort[start.monthValue - 1]}"
        val endStr = "${end.dayOfMonth} ${monthsShort[end.monthValue - 1]}"
        "$startStr - $endStr"
    } else {
        val formatter = DateTimeFormatter.ofPattern(PERIOD)
        "${start.format(formatter)}-${end.format(formatter)}"
    }
}