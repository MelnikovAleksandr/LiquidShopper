package ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard.TaskCard
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksState
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.datetime.toDayMonthString

@Composable
fun TasksList(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    scrollState: LazyListState,
    hazeState: HazeState,
    state: TasksState,
    selectedDayTasks: GroupedTasksByDay?,
    onDeleteTask: (Task) -> Unit,
    onEditDialogShow: (Task) -> Unit,
    onShareTask: (Task) -> Unit,
    isPortrait: Boolean
) {

    LazyColumn(
        modifier = modifier
            .liquefiable(liquidState)
            .fillMaxSize(),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {

        stickyHeader {
            Box(
                modifier = Modifier
                    .hazeEffect(state = hazeState, style = HazeStyle.Unspecified)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            ) {

                Text(
                    modifier = Modifier
                        .then(
                            if (isPortrait) {
                                Modifier
                            } else {
                                Modifier.statusBarsPadding()
                            }
                        )
                        .padding(dimens.small3),
                    text = state.selectedDay.toDayMonthString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (selectedDayTasks?.tasks.isNullOrEmpty()) {
            item {
                BasicText(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    text = buildAnnotatedString {
                        append(stringResource(R.string.empty_tasks))
                        append(" ")
                        appendInlineContent("icon_add", "[add]")
                        append(". ")
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    inlineContent = mapOf(
                        "icon_add" to InlineTextContent(
                            placeholder = Placeholder(
                                width = MaterialTheme.typography.headlineMedium.fontSize,
                                height = MaterialTheme.typography.headlineMedium.fontSize,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                )
            }
        } else {
            items(items = selectedDayTasks?.tasks ?: emptyList(), key = { it.uid }) { task ->
                TaskCard(
                    modifier = Modifier
                        .animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null,
                            placementSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .hazeSource(state = hazeState),
                    liquidState = liquidState,
                    task = task,
                    onShareTask = onShareTask,
                    onEditTask = onEditDialogShow,
                    onDeleteTask = onDeleteTask
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .height((24 + 80).dp)
                    .navigationBarsPadding()
            )
        }
    }
}