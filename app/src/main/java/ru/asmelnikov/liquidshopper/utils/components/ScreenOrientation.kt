package ru.asmelnikov.liquidshopper.utils.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun isPortrait(): Boolean {
    val configuration by rememberUpdatedState(LocalConfiguration.current)
    return configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

@Composable
fun isLandScape(): Boolean {
    val configuration by rememberUpdatedState(LocalConfiguration.current)
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
fun Modifier.navigationBarsPaddingIfLandscape(): Modifier =
    if (isLandScape()) {
        this.navigationBarsPadding()
    } else {
        this
    }

@Composable
fun Modifier.safeDrawingEndPaddingIfLandscape(): Modifier =
    if (isLandScape()) {
        val insets = WindowInsets.safeDrawing.asPaddingValues()
        this.padding(end = insets.calculateEndPadding(LayoutDirection.Ltr))
    } else {
        this
    }