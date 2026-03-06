package ru.asmelnikov.liquidshopper.presentation.details.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.delay
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.LiquidParams
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox

private const val MIN_COUNT = 1
private const val MAX_COUNT = 1000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountSlider(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    liquidParams: LiquidParams = LiquidParams(),
    count: Int,
    onCountChange: (Int) -> Unit
) {

    val nestedLiquidState = rememberLiquidState()
    var isMunisPress by remember {
        mutableStateOf(false)
    }
    var isPlusPress by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isMunisPress, count) {
        while (isMunisPress) {
            if (count > MIN_COUNT) {
                onCountChange(count - 1)
            } else {
                break
            }
            delay(10)
        }
    }

    LaunchedEffect(isPlusPress, count) {
        while (isPlusPress) {
            if (count < MAX_COUNT) {
                onCountChange(count + 1)
            } else {
                break
            }
            delay(10)
        }
    }

    val counter by animateIntAsState(
        targetValue = count,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        ), label = ""
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(dimens.small1),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScaleButtonBox(
                modifier = Modifier.size(dimens.regular),
                liquidState = liquidState,
                isPressed = { isPressed ->
                    isMunisPress = isPressed
                },
                onClick = {
                    if (count > MIN_COUNT) {
                        onCountChange(count - 1)
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(dimens.medium4),
                    painter = painterResource(R.drawable.minus),
                    contentDescription = "minus",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(dimens.small3))
            ScaleButtonBox(
                modifier = Modifier
                    .height(dimens.regular)
                    .weight(1f),
                liquidState = liquidState,
                enabled = false,
                onClick = {}
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = dimens.small3),
                    text = counter.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.width(dimens.small3))
            ScaleButtonBox(
                modifier = Modifier.size(dimens.regular),
                liquidState = liquidState,
                isPressed = { isPressed ->
                    isPlusPress = isPressed
                },
                onClick = {
                    if (count < MAX_COUNT) {
                        onCountChange(count + 1)
                    }
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(dimens.medium4),
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "plus",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = counter.toFloat(),
            valueRange = MIN_COUNT.toFloat()..MAX_COUNT.toFloat(),
            onValueChange = { newIntValue ->
                if (newIntValue.toInt() in MIN_COUNT..MAX_COUNT) {
                    onCountChange(newIntValue.toInt())
                }
            },
            steps = 0,
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.liquefiable(nestedLiquidState),
                    sliderState = sliderState,
                    thumbTrackGapSize = 0.dp,
                    colors = colors(
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        activeTickColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        inactiveTickColor = MaterialTheme.colorScheme.secondary
                    )
                )
            },
            thumb = {
                Box(
                    Modifier
                        .size(dimens.medium4)
                        .clip(CircleShape)
                        .liquid(nestedLiquidState, liquidParams())
                )
            }
        )
    }
}

@Preview
@Composable
private fun CountSliderPreview() {
    LiquidShopperTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            var count by remember {
                mutableIntStateOf(1)
            }

            CountSlider(
                liquidState = rememberLiquidState(),
                count = count,
                onCountChange = {
                    count = it
                }
            )
        }
    }
}