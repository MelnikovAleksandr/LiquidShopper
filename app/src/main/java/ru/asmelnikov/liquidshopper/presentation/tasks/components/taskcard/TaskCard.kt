package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.domain.models.taskItemsMock
import ru.asmelnikov.liquidshopper.domain.models.taskMock
import ru.asmelnikov.liquidshopper.utils.components.liquid.LiquidButtonBox
import ru.asmelnikov.liquidshopper.utils.components.liquid.LiquidButtonColumn
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.navigationBarsPaddingIfLandscape
import ru.asmelnikov.liquidshopper.utils.datetime.toFormattedString

@Composable
fun SharedTransitionScope.TaskCard(
    modifier: Modifier = Modifier,
    liquidState: LayerBackdrop,
    listLiquidState: LayerBackdrop,
    task: Task,
    onShareTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onDetails: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier

            .fillMaxWidth()
            .padding(horizontal = dimens.small1)
            .clip(RoundedCornerShape(dimens.small1))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        task.taskType.color.copy(alpha = 0.7f),
                        task.taskType.color.copy(alpha = 0.4f)
                    )
                )
            )
            .clickable(
                onClick = {
                    onDetails(task.uid)
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = dimens.extraSmall2, end = dimens.small1),
                text = task.timeStamp.toFormattedString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularProgress(
                modifier = Modifier
                    .padding(start = dimens.small3)
                    .padding(vertical = dimens.small1),
                allItemsCount = task.items.count(),
                completedItemCount = task.items.count { it.bought }
            )

            Text(
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = task.taskName + task.uid),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 500)
                        }
                    )
                    .weight(1f)
                    .padding(horizontal = dimens.small1)
                    .padding(top = dimens.small1, bottom = dimens.small3),
                text = task.taskName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 15,
                overflow = TextOverflow.Ellipsis
            )

            Image(
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = task.taskType.drawableRes + task.uid),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 500)
                        }
                    )
                    .size(dimens.medium4),
                painter = painterResource(task.taskType.drawableRes),
                contentDescription = "type"
            )
            Spacer(modifier = Modifier.width(dimens.extraSmall1))
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {

                LiquidButtonBox(
                    onClick = {
                        onShareTask(task)
                    },
                    backdrop = liquidState,
                    modifier = Modifier.size(dimens.medium4),
                    content = {
                        Icon(
                            modifier = Modifier.size(dimens.medium3),
                            imageVector = Icons.Filled.Share,
                            contentDescription = "share",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                Spacer(modifier = Modifier.width(dimens.extraSmall1))
                LiquidButtonColumn(
                    onClick = {
                        expanded = true
                    },
                    backdrop = liquidState,
                    modifier = Modifier.navigationBarsPaddingIfLandscape().size(dimens.medium4),
                    content = {
                        DropDown(
                            modifier = Modifier,
                            liquidState = rememberCombinedBackdrop(listLiquidState, liquidState),
                            expanded = expanded,
                            onDismiss = {
                                expanded = false
                            },
                            onEditClick = {
                                onEditTask(task)
                            },
                            onDeleteClick = {
                                onDeleteTask(task)
                            }
                        )

                        Icon(
                            modifier = Modifier.size(dimens.medium3),
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "menu",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                Spacer(modifier = Modifier.width(dimens.extraSmall1))
            }
        }
    }
}

@Preview
@Composable
private fun TaskCardPreview() {
    LiquidShopperTheme(darkTheme = true) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(visible = true) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    TaskCard(
                        task = taskMock.copy(items = taskItemsMock),
                        liquidState = rememberLayerBackdrop(),
                        listLiquidState = rememberLayerBackdrop(),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        onShareTask = {},
                        onEditTask = {},
                        onDeleteTask = {},
                        onDetails = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TaskCard(
                        task = taskMock.copy(
                            items = taskItemsMock,
                            taskType = TaskTypes.ALCOHOL,
                            taskName = "Test name of Task"
                        ),
                        liquidState = rememberLayerBackdrop(),
                        listLiquidState = rememberLayerBackdrop(),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        onShareTask = {},
                        onEditTask = {},
                        onDeleteTask = {},
                        onDetails = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}