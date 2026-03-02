package ru.asmelnikov.liquidshopper.presentation.tasks.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.github.fletchmckee.liquid.LiquidScope
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid
import ru.asmelnikov.liquidshopper.presentation.tasks.ScaleIndication

@Composable
fun ScaleButtonBox(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    liquidParams: LiquidParams = LiquidParams(),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = ScaleIndication,
                interactionSource = interactionSource,
                enabled = enabled
            )
            .liquid(liquidState, liquidParams()),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun ScaleButtonRow(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    liquidParams: LiquidParams = LiquidParams(),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = ScaleIndication,
                interactionSource = interactionSource,
                enabled = enabled
            )
            .liquid(liquidState, liquidParams()),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun ScaleButtonColumn(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    liquidParams: LiquidParams = LiquidParams(),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column (
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = ScaleIndication,
                interactionSource = interactionSource,
                enabled = enabled
            )
            .liquid(liquidState, liquidParams()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content
    )
}

data class LiquidParams(
    val refraction: Float = 0.5f,
    val curve: Float = 0.4f,
    val saturation: Float = 1.0f,
    val dispersion: Float = 0f,
    val edge: Float = 0.07f,
    val shape: Shape = CircleShape
) {
    operator fun invoke(): LiquidScope.() -> Unit = {
        refraction = this@LiquidParams.refraction
        curve = this@LiquidParams.curve
        saturation = this@LiquidParams.saturation
        dispersion = this@LiquidParams.dispersion
        edge = this@LiquidParams.edge
        shape = this@LiquidParams.shape
    }
}

//refraction = 0.5f
//curve = 0.4f
//saturation = 1.0f
//dispersion = 1.0f
//edge = 0.15f
//shape = CircleShape