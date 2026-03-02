package ru.asmelnikov.liquidshopper.presentation.tasks

import android.widget.FrameLayout
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState
import ru.asmelnikov.liquidshopper.presentation.tasks.components.ScaleButtonBox
import ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard.TaskCard
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


@Composable
fun TasksScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    viewModel: TasksViewModel = koinViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsState()

    TasksScreenContent(
        state = state,
        insertTask = {
            viewModel.insertTask(
                Task(
                    uid = UUID.randomUUID().hashCode(),
                    taskName = it,
                    taskType = TaskTypes.OTHER,
                    timeStamp = LocalDateTime.now(),
                    items = listOf()
                )
            )
        },
        onDeleteTask = viewModel::deleteTask
    )

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreenContent(
    state: TasksState,
    insertTask: (String) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    val scrollState = rememberLazyListState()

    var isModalShow by rememberSaveable {
        mutableStateOf(false)
    }

    var text1 by rememberSaveable {
        mutableStateOf("")
    }

    var text2 by rememberSaveable {
        mutableStateOf("")
    }

    val liquidState = rememberLiquidState()

    if (isModalShow) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            modifier = Modifier,
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        isModalShow = false
                        text1 = ""
                        text2 = ""
                    }
                }
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .padding(top = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconButton(
                        modifier = Modifier
                            .liquid(liquidState) {
                                refraction = 0.5f
                                curve = 0.4f
                                saturation = 1.0f
                                dispersion = 1.0f
                                edge = 0.15f
                                shape = CircleShape
                            },
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    isModalShow = false
                                    text1 = ""
                                    text2 = ""
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = Color.White.copy(alpha = 0.54f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquid(liquidState) {
                            refraction = 0.5f
                            curve = 0.4f
                            saturation = 1.0f
                            dispersion = 1.0f
                            edge = 0.15f
                            shape = RoundedCornerShape(8.dp)
                        },
                    value = text1,
                    onValueChange = {
                        text1 = it
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquid(liquidState) {
                            refraction = 0.5f
                            curve = 0.4f
                            saturation = 1.0f
                            dispersion = 1.0f
                            edge = 0.15f
                            shape = RoundedCornerShape(8.dp)
                        },
                    value = text2,
                    onValueChange = {
                        text2 = it
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                IconButton(
                    modifier = Modifier
                        .liquid(liquidState) {
                            refraction = 0.5f
                            curve = 0.4f
                            saturation = 1.0f
                            dispersion = 1.0f
                            edge = 0.15f
                            shape = CircleShape
                        },
                    shape = CircleShape,
                    onClick = {
                        scope.launch {
                            scrollState.animateScrollToItem(0)
                        }
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                isModalShow = false
                                text1 = ""
                                text2 = ""
                            }
                        }
                        insertTask(text1)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "add"
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        VideoBackground(
            videoResId = R.raw.back_video,
            modifier = Modifier.fillMaxSize()
        )

        LazyColumn(
            modifier = Modifier
                .liquefiable(liquidState)
                .fillMaxSize(),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .statusBarsPadding()
                        .height(80.dp)
                )
            }


            items(items = state.tasks, key = { it.uid }) { task ->
                TaskCard(
                    modifier = Modifier.animateItem(),
                    liquidState = liquidState,
                    task = task,
                    onShareTask = {},
                    onEditTask = {},
                    onDeleteTask = {
                        onDeleteTask(task)
                    }
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height((24 + 80).dp)
                        .navigationBarsPadding()
                )
            }

        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier

                    .liquid(liquidState) {
                        refraction = 0.5f
                        curve = 0.4f
                        saturation = 1.0f
                        dispersion = 0f
                        edge = 0.07f
                        shape = CircleShape
                    }
                    .size(80.dp),
                shape = CircleShape,
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "add"
                )
            }
            IconButton(
                modifier = Modifier

                    .liquid(liquidState) {
                        refraction = 0.5f
                        curve = 0.4f
                        saturation = 1.0f
                        dispersion = 0f
                        edge = 0.07f
                        shape = CircleShape
                    }
                    .size(80.dp),
                shape = CircleShape,
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.Build,
                    contentDescription = "add"
                )
            }
            IconButton(
                modifier = Modifier
                    .size(80.dp)
                    .liquid(liquidState) {
                        refraction = 0.5f
                        curve = 0.4f
                        saturation = 1.0f
                        dispersion = 0f
                        edge = 0.07f
                        shape = CircleShape
                    },
                shape = CircleShape,
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.Home,
                    contentDescription = "add"
                )
            }

        }

        ScaleButtonBox(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(80.dp),
            liquidState = liquidState,
            onClick = {
                isModalShow = !isModalShow
            }) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "add"
            )
        }

    }
}

fun LocalDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    return this.format(formatter)
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoBackground(
    videoResId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("android.resource://${context.packageName}/$videoResId".toUri()))
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 0f
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = modifier
    )
}

object ScaleIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleNode(interactionSource)
    }

    override fun equals(other: Any?): Boolean = other === ScaleIndication
    override fun hashCode() = 100
}

private class ScaleNode(private val interactionSource: InteractionSource) :
    Modifier.Node(), DrawModifierNode {

    var currentPressPosition: Offset = Offset.Zero
    val animatedScalePercent = Animatable(1f)

    private suspend fun animateToPressed(pressPosition: Offset) {
        currentPressPosition = pressPosition
        animatedScalePercent.animateTo(0.8f, spring())
    }

    private suspend fun animateToResting() {
        animatedScalePercent.animateTo(1f, spring())
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                    is PressInteraction.Release -> animateToResting()
                    is PressInteraction.Cancel -> animateToResting()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(
            scale = animatedScalePercent.value,
            pivot = currentPressPosition
        ) {
            this@draw.drawContent()
        }
    }
}