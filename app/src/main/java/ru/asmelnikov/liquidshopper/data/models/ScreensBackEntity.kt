package ru.asmelnikov.liquidshopper.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScreensBackEntity(
    @PrimaryKey
    val screenId: Int,
    val backImageId: Int
)
