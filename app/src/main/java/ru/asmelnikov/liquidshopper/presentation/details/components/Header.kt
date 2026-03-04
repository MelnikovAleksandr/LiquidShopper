package ru.asmelnikov.liquidshopper.presentation.details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.rememberHazeState
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.taskMock
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsState
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox
import java.util.UUID

@Composable
fun SharedTransitionScope.Header(
    modifier: Modifier = Modifier,
    state: ItemsState,
    hazeState: HazeState,
    liquidState: LiquidState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit
) {

    Box(
        modifier = modifier
            .hazeEffect(state = hazeState, style = HazeStyle.Unspecified)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
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
                onClick = onBackClick
            ) {
                Icon(
                    modifier = Modifier.size(dimens.medium4),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "prev",
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
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(
                                key = (state.task?.taskName ?: "") + state.task?.uid
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 500)
                            }
                        )
                        .padding(horizontal = dimens.small3),
                    text = state.task?.taskName ?: "",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.width(dimens.small3))
            androidx.compose.animation.AnimatedVisibility(
                enter = scaleIn(),
                exit = scaleOut(),
                visible = state.task != null
            ) {
                ScaleButtonBox(
                    modifier = Modifier.size(dimens.regular),
                    liquidState = liquidState,
                    enabled = false,
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(
                                    key = (state.task?.taskType?.drawableRes
                                        ?: 0) + (state.task?.uid ?: 0)
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 500)
                                }
                            )
                            .size(dimens.medium4),
                        painter = painterResource(
                            state.task?.taskType?.drawableRes ?: R.string.category_other
                        ),
                        contentDescription = "type",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HeaderPreview1() {
    LiquidShopperTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Header(
                        state = ItemsState(
                            taskId = UUID.randomUUID().hashCode(),
                            task = taskMock
                        ),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        hazeState = rememberHazeState(),
                        liquidState = rememberLiquidState(),
                        onBackClick = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HeaderPreview2() {
    LiquidShopperTheme(darkTheme = true) {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Header(
                        state = ItemsState(
                            taskId = UUID.randomUUID().hashCode(),
                            task = taskMock
                        ),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        hazeState = rememberHazeState(),
                        liquidState = rememberLiquidState(),
                        onBackClick = {}
                    )
                }
            }
        }
    }
}