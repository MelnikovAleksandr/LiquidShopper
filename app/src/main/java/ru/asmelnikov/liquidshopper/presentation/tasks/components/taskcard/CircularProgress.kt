package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier,
    allItemsCount: Int,
    completedItemCount: Int
) {
    val colorScheme = MaterialTheme.colorScheme
    val durationMillis = remember { 500 }
    val targetProgress = if (allItemsCount > 0) {
        completedItemCount.toFloat() / allItemsCount.toFloat()
    } else 0f
    var currentProgress by remember { mutableFloatStateOf(0f) }
    val animatedCurrentProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
        label = "progress_animation"
    )
    LaunchedEffect(completedItemCount, allItemsCount) {
        currentProgress = targetProgress
    }

    val isCompletedAll by remember(allItemsCount, completedItemCount) {
        mutableStateOf(allItemsCount == completedItemCount && allItemsCount > 0)
    }

    Box(
        modifier = modifier
            .size(72.dp)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = { animatedCurrentProgress },
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.secondary,
            strokeWidth = dimens.small1
        )

        BasicText(
            modifier = Modifier.padding(dimens.small3),
            text = "${completedItemCount}/${allItemsCount}",
            style = MaterialTheme.typography.labelMedium,
            color = ColorProducer {
                if (isCompletedAll) colorScheme.primary else colorScheme.onSecondary
            },
            autoSize = TextAutoSize.StepBased(
                minFontSize = MaterialTheme.typography.labelSmall.fontSize
            ),
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun CircularProgressPreview() {
    LiquidShopperTheme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgress(
                allItemsCount = 10,
                completedItemCount = 5
            )
        }
    }
}