package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskmodal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import io.github.fletchmckee.liquid.LiquidState
import kotlinx.coroutines.launch
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.presentation.tasks.TasksState
import ru.asmelnikov.liquidshopper.presentation.tasks.components.LiquidParams
import ru.asmelnikov.liquidshopper.presentation.tasks.components.ScaleButtonBox
import ru.asmelnikov.liquidshopper.presentation.tasks.components.ScaleButtonRow
import ru.asmelnikov.liquidshopper.presentation.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskModal(
    modifier: Modifier = Modifier,
    state: TasksState,
    liquidState: LiquidState,
    liquidParams: LiquidParams,
    scrollState: LazyListState,
    onDismissRequest: () -> Unit,
    onTitleChange: (String) -> Unit,
    onTypeChange: (TaskTypes) -> Unit,
    onTaskCreateConfirm: () -> Unit
) {

    if (state.isModalShow) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        LaunchedEffect(state.successCreate) {
            if (state.successCreate) {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismissRequest()
                    }
                }
            }
        }

        ModalBottomSheet(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismissRequest()
                    }
                }
            },
            contentWindowInsets = { WindowInsets.ime },
            dragHandle = {}
        ) {

            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    ScaleButtonBox(
                        modifier = Modifier
                            .padding(dimens.small1)
                            .size(dimens.medium4),
                        liquidState = liquidState,
                        liquidParams = liquidParams,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismissRequest()
                                }
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(dimens.medium3),
                            imageVector = Icons.Filled.Close,
                            contentDescription = "close"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimens.small3))

                TextFieldCustom(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.small1),
                    title = state.title,
                    onTitleChange = onTitleChange,
                    isEmptyError = state.emptyTitleError,
                    emptyPlaceholder = stringResource(R.string.task_placeholder),
                    focusManager = focusManager,
                    keyboardController = keyboardController
                )
                Spacer(modifier = Modifier.height(dimens.small3))

                EnumDropDown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.small1),
                    items = TaskTypes.entries,
                    onItemChanged = {
                        onTypeChange(it as TaskTypes)
                    },
                    selectedItem = state.taskType,
                    focusManager = focusManager,
                    keyboardController = keyboardController
                )

                Spacer(modifier = Modifier.height(dimens.small3))

                ScaleButtonRow(
                    modifier = Modifier
                        .padding(dimens.small1)
                        .navigationBarsPadding(),
                    liquidState = liquidState,
                    liquidParams = liquidParams,
                    onClick = {
                        onTaskCreateConfirm()
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(dimens.regular)
                            .padding(dimens.extraSmall2),
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "add"
                    )
                    Text(
                        modifier = Modifier.padding(end = dimens.small1),
                        text = stringResource(R.string.task_create),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(dimens.small3))
            }
        }
    }
}