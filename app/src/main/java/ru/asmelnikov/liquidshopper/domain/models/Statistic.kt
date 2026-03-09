package ru.asmelnikov.liquidshopper.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Statistic(
    val dataMapSet: Map<TaskTypes, Int>,
    val legendMapSet: Map<TaskTypes, Int>,
    val itemsByDay: List<GroupedItemsByDay>
): Parcelable

@Parcelize
data class GroupedItemsByDay(
    val day: LocalDate,
    val itemsCount: Int,
    val items: List<Item>
): Parcelable