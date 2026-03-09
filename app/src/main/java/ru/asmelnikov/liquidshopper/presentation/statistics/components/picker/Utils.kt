package ru.asmelnikov.liquidshopper.presentation.statistics.components.picker

import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

fun isInDateBetweenSelection(
    inDate: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate,
): Boolean {
    if (startDate.yearMonth == endDate.yearMonth) return false
    if (inDate.yearMonth == startDate.yearMonth) return true
    val firstDateInThisMonth = inDate.yearMonth.nextMonth.atStartOfMonth()
    return firstDateInThisMonth in startDate..endDate && startDate != firstDateInThisMonth
}

fun isOutDateBetweenSelection(
    outDate: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate,
): Boolean {
    if (startDate.yearMonth == endDate.yearMonth) return false
    if (outDate.yearMonth == endDate.yearMonth) return true
    val lastDateInThisMonth = outDate.yearMonth.previousMonth.atEndOfMonth()
    return lastDateInThisMonth in startDate..endDate && endDate != lastDateInThisMonth
}

fun getSelection(
    clickedDate: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?
): Pair<LocalDate?, LocalDate?> {
    return if (startDate != null) {
        if (clickedDate < startDate || endDate != null) {
            clickedDate to null
        } else if (clickedDate != startDate) {
            startDate to clickedDate
        } else {
            clickedDate to null
        }
    } else {
        clickedDate to null
    }
}

fun checkDaysBetween(startDate: LocalDate?, endDate: LocalDate?): Long? {
    return if (startDate == null || endDate == null) {
        null
    } else {
        ChronoUnit.DAYS.between(startDate, endDate)
    }
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.getDefault()).let { value ->
        if (uppercase) value.uppercase(Locale.getDefault()) else value
    }
}