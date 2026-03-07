package ru.asmelnikov.liquidshopper.presentation.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState
import ru.asmelnikov.liquidshopper.presentation.navigation.Routes
import ru.asmelnikov.liquidshopper.presentation.navigation.navigate
import ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar.ChoseMonthDialog
import ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar.Day
import ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar.MonthsCalendarTitle
import ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar.TasksList
import ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar.rememberFirstCompletelyVisibleMonth
import ru.asmelnikov.liquidshopper.presentation.tasks.components.edittaskmodal.EditTaskModal
import ru.asmelnikov.liquidshopper.presentation.tasks.components.newtaskmodal.NewTaskModal
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksSideEffects
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksState
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksViewModel
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.LiquidParams
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox
import ru.asmelnikov.liquidshopper.utils.components.isPortrait
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Composable
fun SharedTransitionScope.TasksScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: TasksViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is TasksSideEffects.NavigateToDetails -> {
                appState.navigate(route = Routes.ItemsScreen(taskId = it.taskId))
            }
        }
    }

    TasksScreenContent(
        state = state,
        onDeleteTask = viewModel::deleteTask,
        onDismissModal = viewModel::onModalDismiss,
        onCreateClick = viewModel::onCreateClick,
        onTitleChange = viewModel::onTitleChange,
        onTypeChange = viewModel::onTaskTypeChange,
        onTaskCreateConfirm = viewModel::onConfirmCreateClick,
        onSelectedDayChange = viewModel::onSelectedDayChange,
        onTimeStampChange = viewModel::onTimeStampChange,
        onDismissMonthDialog = viewModel::onDismissMonthDialog,
        onMonthDialogShow = viewModel::onMonthDialogShow,
        onEditDialogShow = viewModel::onEditClick,
        onDismissEditRequest = viewModel::onEditDismiss,
        onEditTitleChange = viewModel::onEditTitle,
        onEditTypeChange = viewModel::onEditType,
        onEditTimeStampChange = viewModel::onEditTimeStamp,
        onTaskUpdateConfirm = viewModel::onConfirmEditClick,
        onTaskShare = { task ->
            viewModel.onTaskShare(task = task, context = context)
        },
        onDetails = viewModel::navigateToDetails,
        isPortrait = isPortrait(),
        animatedVisibilityScope = animatedVisibilityScope
    )

}

@Composable
fun SharedTransitionScope.TasksScreenContent(
    state: TasksState,
    onDeleteTask: (Task) -> Unit,
    onDismissModal: () -> Unit,
    onCreateClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onTypeChange: (TaskTypes) -> Unit,
    onTaskCreateConfirm: () -> Unit,
    onSelectedDayChange: (LocalDate) -> Unit,
    onTimeStampChange: (LocalTime) -> Unit,
    onDismissMonthDialog: () -> Unit,
    onMonthDialogShow: () -> Unit,
    onEditDialogShow: (Task) -> Unit,
    onDismissEditRequest: () -> Unit,
    onEditTitleChange: (String) -> Unit,
    onEditTypeChange: (TaskTypes) -> Unit,
    onEditTimeStampChange: (LocalDateTime) -> Unit,
    onTaskUpdateConfirm: () -> Unit,
    onTaskShare: (Task) -> Unit,
    onDetails: (Int) -> Unit,
    isPortrait: Boolean,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val scrollState = rememberLazyListState()
    val liquidState = rememberLiquidState()
    val hazeState = rememberHazeState()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(300) }
    val endMonth = remember { currentMonth.plusMonths(300) }
    val daysOfWeek = remember { daysOfWeek() }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstCompletelyVisibleMonth(calendarState)
    val selectedDayTasks by remember(state.tasks, state.selectedDay) {
        val cache = mutableMapOf<LocalDate, GroupedTasksByDay?>()

        derivedStateOf {
            cache.getOrPut(state.selectedDay) {
                state.tasks.find { it.start == state.selectedDay }
            }
        }
    }

    val density = LocalDensity.current
    val statusBarHeight = with(density) {
        WindowInsets.statusBars.getTop(density).toDp()
    }

    ChoseMonthDialog(
        modifier = Modifier,
        state = state,
        onDismissRequest = onDismissMonthDialog,
        liquidState = liquidState,
        chosenMonth = { date ->
            coroutineScope.launch {
                calendarState.animateScrollToMonth(date.yearMonth)
            }
            onSelectedDayChange(date)
        }
    )

    NewTaskModal(
        state = state,
        liquidState = liquidState,
        onTypeChange = onTypeChange,
        onDismissRequest = onDismissModal,
        onTitleChange = onTitleChange,
        onTaskCreateConfirm = onTaskCreateConfirm,
        onTimeStampChange = onTimeStampChange
    )

    EditTaskModal(
        state = state,
        liquidState = liquidState,
        onDismissRequest = onDismissEditRequest,
        onTitleChange = onEditTitleChange,
        onTypeChange = onEditTypeChange,
        onTimeStampChange = onEditTimeStampChange,
        onTaskUpdateConfirm = onTaskUpdateConfirm
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.background_pattern_1),
            contentDescription = null,
            modifier = Modifier
                .liquefiable(liquidState)
                .hazeSource(state = hazeState)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .hazeEffect(state = hazeState, style = HazeStyle.Unspecified)
                    .fillMaxWidth()
                    .height(statusBarHeight)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            )
            if (isPortrait) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    MonthsCalendarTitle(
                        modifier = Modifier,
                        currentMonth = visibleMonth.yearMonth,
                        liquidState = liquidState,
                        goToPrevious = {
                            coroutineScope.launch {
                                calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.previousMonth)
                            }
                        },
                        goToNext = {
                            coroutineScope.launch {
                                calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.nextMonth)
                            }
                        },
                        onMonthDialogShow = onMonthDialogShow
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f))
                                .hazeEffect(state = hazeState, style = HazeStyle.Unspecified)
                        )

                        HorizontalCalendar(
                            modifier = Modifier
                                .matchParentSize(),
                            contentHeightMode = ContentHeightMode.Fill,
                            state = calendarState,
                            dayContent = { day ->
                                var currentDayTasks by remember {
                                    mutableStateOf<GroupedTasksByDay?>(
                                        null
                                    )
                                }
                                LaunchedEffect(key1 = state.tasks, key2 = day) {
                                    withContext(Dispatchers.IO) {
                                        currentDayTasks =
                                            if (day.position == DayPosition.MonthDate) {
                                                state.tasks.find { taskByDay ->
                                                    taskByDay.start == day.date
                                                }
                                            } else {
                                                null
                                            }
                                    }
                                }
                                Day(
                                    modifier = Modifier,
                                    day = day,
                                    groupedTasks = currentDayTasks,
                                    onClick = onSelectedDayChange,
                                    liquidState = liquidState
                                )
                                androidx.compose.animation.AnimatedVisibility(
                                    modifier = Modifier.align(Alignment.Center),
                                    enter = scaleIn(),
                                    exit = scaleOut(),
                                    visible = day.date == state.selectedDay
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .fillMaxSize()
                                            .clip(MaterialTheme.shapes.large)
                                            .liquid(
                                                liquidState, LiquidParams(
                                                    refraction = 0.3f,
                                                    edge = 0.2f,
                                                    shape = MaterialTheme.shapes.large
                                                ).invoke()
                                            )
                                    )
                                }
                            }
                        )
                    }
                    TasksList(
                        liquidState = liquidState,
                        scrollState = scrollState,
                        hazeState = hazeState,
                        state = state,
                        animatedVisibilityScope = animatedVisibilityScope,
                        selectedDayTasks = selectedDayTasks,
                        onDeleteTask = onDeleteTask,
                        onShareTask = onTaskShare,
                        onEditDialogShow = onEditDialogShow,
                        onDetails = onDetails
                    )

                }
            } else {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        MonthsCalendarTitle(
                            modifier = Modifier,
                            currentMonth = visibleMonth.yearMonth,
                            liquidState = liquidState,
                            goToPrevious = {
                                coroutineScope.launch {
                                    calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.previousMonth)
                                }
                            },
                            goToNext = {
                                coroutineScope.launch {
                                    calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.nextMonth)
                                }
                            },
                            onMonthDialogShow = onMonthDialogShow
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f))
                                    .hazeEffect(state = hazeState, style = HazeStyle.Unspecified)
                            )

                            HorizontalCalendar(
                                modifier = Modifier
                                    .matchParentSize(),
                                contentHeightMode = ContentHeightMode.Fill,
                                state = calendarState,
                                dayContent = { day ->
                                    var currentDayTasks by remember {
                                        mutableStateOf<GroupedTasksByDay?>(
                                            null
                                        )
                                    }
                                    LaunchedEffect(key1 = state.tasks, key2 = day) {
                                        withContext(Dispatchers.IO) {
                                            currentDayTasks =
                                                if (day.position == DayPosition.MonthDate) {
                                                    state.tasks.find { taskByDay ->
                                                        taskByDay.start == day.date
                                                    }
                                                } else {
                                                    null
                                                }
                                        }
                                    }
                                    Day(
                                        modifier = Modifier,
                                        day = day,
                                        groupedTasks = currentDayTasks,
                                        onClick = onSelectedDayChange,
                                        liquidState = liquidState
                                    )
                                    androidx.compose.animation.AnimatedVisibility(
                                        modifier = Modifier.align(Alignment.Center),
                                        enter = scaleIn(),
                                        exit = scaleOut(),
                                        visible = day.date == state.selectedDay && day.position == DayPosition.MonthDate
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .fillMaxSize()
                                                .clip(MaterialTheme.shapes.large)
                                                .liquid(
                                                    liquidState, LiquidParams(
                                                        refraction = 0.3f,
                                                        edge = 0.2f,
                                                        shape = MaterialTheme.shapes.large
                                                    ).invoke()
                                                )
                                        )
                                    }
                                }
                            )
                        }
                    }
                    TasksList(
                        modifier = Modifier
                            .weight(1f),
                        liquidState = liquidState,
                        scrollState = scrollState,
                        hazeState = hazeState,
                        state = state,
                        animatedVisibilityScope = animatedVisibilityScope,
                        selectedDayTasks = selectedDayTasks,
                        onDeleteTask = onDeleteTask,
                        onShareTask = onTaskShare,
                        onEditDialogShow = onEditDialogShow,
                        onDetails = onDetails
                    )
                }
            }
        }

        ScaleButtonBox(
            modifier = Modifier
                .navigationBarsPadding()
                .align(Alignment.BottomEnd)
                .padding(dimens.medium2)
                .size(dimens.regular),
            liquidState = liquidState,
            onClick = onCreateClick
        ) {
            Icon(
                modifier = Modifier
                    .size(dimens.medium4),
                imageVector = Icons.Filled.Add,
                contentDescription = "add",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun CalendarPreview1() {
    LiquidShopperTheme(darkTheme = true) {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = true) {
                TasksScreenContent(
                    state = TasksState(),
                    onDeleteTask = {},
                    onDismissModal = {},
                    onCreateClick = {},
                    onTitleChange = {},
                    onTypeChange = {},
                    onTaskCreateConfirm = {},
                    onSelectedDayChange = {},
                    onTimeStampChange = {},
                    onDismissMonthDialog = {},
                    onMonthDialogShow = {},
                    onEditDialogShow = {},
                    onDismissEditRequest = {},
                    onEditTitleChange = {},
                    onEditTypeChange = {},
                    onEditTimeStampChange = {},
                    onTaskUpdateConfirm = {},
                    onTaskShare = {},
                    onDetails = {},
                    isPortrait = true,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun CalendarPreview2() {
    LiquidShopperTheme {
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = true) {
                TasksScreenContent(
                    state = TasksState(),
                    onDeleteTask = {},
                    onDismissModal = {},
                    onCreateClick = {},
                    onTitleChange = {},
                    onTypeChange = {},
                    onTaskCreateConfirm = {},
                    onSelectedDayChange = {},
                    onTimeStampChange = {},
                    onDismissMonthDialog = {},
                    onMonthDialogShow = {},
                    onEditDialogShow = {},
                    onDismissEditRequest = {},
                    onEditTitleChange = {},
                    onEditTypeChange = {},
                    onEditTimeStampChange = {},
                    onTaskUpdateConfirm = {},
                    onTaskShare = {},
                    onDetails = {},
                    isPortrait = false,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}