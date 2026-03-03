package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.domain.models.taskMock
import ru.asmelnikov.liquidshopper.domain.models.taskItemsMock
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.LiquidParams
import ru.asmelnikov.liquidshopper.utils.components.scaleindication.ScaleIndication
import ru.asmelnikov.liquidshopper.utils.datetime.toFormattedString

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    task: Task,
    onShareTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    interactionSource: MutableInteractionSource? = null
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clickable(
                onClick = {},
                indication = ScaleIndication,
                interactionSource = interactionSource,
                enabled = true
            )
            .fillMaxWidth()
            .padding(horizontal = dimens.small1)
            .clip(RoundedCornerShape(dimens.small1))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = dimens.small1, end = dimens.small1),
                text = task.timeStamp.toFormattedString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Row(
            modifier = Modifier,
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
                    .weight(1f)
                    .padding(horizontal = dimens.small1)
                    .padding(top = dimens.small1, bottom = dimens.small3),
                text = task.taskName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                overflow = TextOverflow.Ellipsis
            )

            Image(
                modifier = Modifier.size(dimens.medium4),
                painter = painterResource(task.taskType.drawableRes),
                contentDescription = "type"
            )

            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                IconButton(
                    modifier = Modifier,
                    onClick = {
                        onShareTask(task)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.medium3),
                        imageVector = Icons.Filled.Share,
                        contentDescription = "share",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                IconButton(
                    modifier = Modifier,
                    onClick = {
                        expanded = true
                    }) {
                    DropDown(
                        modifier = modifier,
                        liquidState = liquidState,
                        liquidParams = LiquidParams().copy(dispersion = 1f),
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
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskCardPreview() {
    LiquidShopperTheme(darkTheme = true) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            TaskCard(
                task = taskMock.copy(items = taskItemsMock),
                liquidState = rememberLiquidState(),
                onShareTask = {},
                onEditTask = {},
                onDeleteTask = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            TaskCard(
                task = taskMock.copy(
                    items = taskItemsMock,
                    taskType = TaskTypes.ALCOHOL,
                    taskName = "Test name of Task"
                ),
                liquidState = rememberLiquidState(),
                onShareTask = {},
                onEditTask = {},
                onDeleteTask = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}