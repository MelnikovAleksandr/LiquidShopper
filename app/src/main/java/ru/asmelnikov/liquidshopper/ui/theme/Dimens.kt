package ru.asmelnikov.liquidshopper.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val borderSize: Dp = 1.dp,
    val extraSmall1: Dp = 0.dp,
    val extraSmall2: Dp = 0.dp,
    val small1: Dp = 0.dp,
    val small2: Dp = 0.dp,
    val small3: Dp = 0.dp,
    val medium1: Dp = 0.dp,
    val medium2: Dp = 0.dp,
    val medium3: Dp = 0.dp,
    val medium4: Dp = 0.dp,
    val large: Dp = 0.dp
)

val CompactDimens = Dimens(
    extraSmall1 = 4.dp,
    extraSmall2 = 6.dp,
    small1 = 8.dp,
    small2 = 10.dp,
    small3 = 12.dp,
    medium1 = 16.dp,
    medium2 = 24.dp,
    medium3 = 32.dp,
    medium4 = 40.dp,
    large = 80.dp
)

val MediumDimens = Dimens(
    borderSize = 2.dp,
    extraSmall1 = 6.dp,
    extraSmall2 = 8.dp,
    small1 = 12.dp,
    small2 = 14.dp,
    small3 = 16.dp,
    medium1 = 24.dp,
    medium2 = 32.dp,
    medium3 = 40.dp,
    medium4 = 48.dp,
    large = 100.dp
)

