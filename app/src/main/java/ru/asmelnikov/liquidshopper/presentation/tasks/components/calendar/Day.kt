package ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.taskMock
import ru.asmelnikov.liquidshopper.domain.models.tasksListMock
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun Day(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    groupedTasks: GroupedTasksByDay?,
    liquidState: LiquidState,
    onClick: (LocalDate) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val isToday = remember(day) {
        day.date == LocalDate.now() && day.position == DayPosition.MonthDate
    }
    val textColor = remember(day) {
        when (day.position) {
            DayPosition.MonthDate -> colorScheme.onBackground
            DayPosition.InDate, DayPosition.OutDate -> Color.Unspecified
        }
    }
    Column(
        modifier = modifier
            .liquefiable(liquidState)
            .fillMaxSize()
            .aspectRatio(1f)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onClick(day.date)
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier,
            text = when (day.position) {
                DayPosition.MonthDate -> day.date.dayOfMonth.toString()
                DayPosition.InDate, DayPosition.OutDate -> ""
            },
            color = when (day.date.dayOfWeek) {
                DayOfWeek.SATURDAY, DayOfWeek.SUNDAY -> Color(0xFFFF9A9A)
                else -> {
                    if (isToday) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        textColor
                    }
                }
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.extraSmall2)
                .padding(horizontal = dimens.extraSmall2)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = (groupedTasks?.tasksCount ?: 0) > 0
            ) {
                groupedTasks?.takeIf { it.tasksCount > 0 }?.let {
                    TasksLineIndicator(
                        modifier = Modifier,
                        task = groupedTasks
                    )
                }
            }
        }
    }
}

@Composable
fun TasksLineIndicator(
    modifier: Modifier = Modifier,
    task: GroupedTasksByDay
) {
    val colorScheme = MaterialTheme.colorScheme
    val completedCount = remember(task) {
        task.tasks.count { it.isCompleted }
    }
    val targetProgress = remember(task) {
        if (task.tasksCount > 0) {
            completedCount.toFloat() / task.tasksCount.toFloat()
        } else {
            0f
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 200),
        label = "progressAnimation"
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.extraSmall2)
            .clip(CircleShape),
        color = colorScheme.primary,
        trackColor = colorScheme.errorContainer,
        strokeCap = StrokeCap.Round,
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

@Preview
@Composable
private fun DayPreview() {
    LiquidShopperTheme(darkTheme = true) {
        val liquidState = rememberLiquidState()
        Image(
            painter = painterResource(R.drawable.main_back),
            contentDescription = null,
            modifier = Modifier
                .liquefiable(liquidState)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Day(
                    modifier = Modifier,
                    day = CalendarDay(
                        date = LocalDate.now(),
                        position = DayPosition.MonthDate
                    ),
                    groupedTasks = GroupedTasksByDay(
                        start = LocalDate.now(),
                        tasksCount = 3,
                        tasks = tasksListMock.take(3)
                    ),
                    onClick = {},
                    liquidState = liquidState
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Day(
                    modifier = Modifier,
                    day = CalendarDay(
                        date = LocalDate.now().minusDays(1),
                        position = DayPosition.MonthDate
                    ),
                    groupedTasks = GroupedTasksByDay(
                        start = LocalDate.now(),
                        tasksCount = 1,
                        tasks = listOf(taskMock)
                    ),
                    onClick = {},
                    liquidState = liquidState
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Day(
                    modifier = Modifier,
                    day = CalendarDay(
                        date = LocalDate.now().minusDays(1),
                        position = DayPosition.MonthDate
                    ),
                    groupedTasks = GroupedTasksByDay(
                        start = LocalDate.now(),
                        tasksCount = 2,
                        tasks = tasksListMock.take(2)
                    ),
                    onClick = {},
                    liquidState = liquidState,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Day(
                    modifier = Modifier,
                    day = CalendarDay(
                        date = LocalDate.now().minusDays(1),
                        position = DayPosition.MonthDate
                    ),
                    groupedTasks = GroupedTasksByDay(
                        start = LocalDate.now(),
                        tasksCount = 0,
                        tasks = emptyList()
                    ),
                    onClick = {},
                    liquidState = liquidState,
                )
            }
        }
    }
}