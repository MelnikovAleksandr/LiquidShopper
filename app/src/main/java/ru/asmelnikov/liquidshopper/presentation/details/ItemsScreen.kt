package ru.asmelnikov.liquidshopper.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.UnitType
import ru.asmelnikov.liquidshopper.presentation.details.components.CreateItemModal
import ru.asmelnikov.liquidshopper.presentation.details.components.EditItemModal
import ru.asmelnikov.liquidshopper.presentation.details.components.Header
import ru.asmelnikov.liquidshopper.presentation.details.components.ItemView
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsSideEffects
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsState
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsViewModel
import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState
import ru.asmelnikov.liquidshopper.presentation.navigation.popUp
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox
import ru.asmelnikov.liquidshopper.utils.components.isPortrait

@Composable
fun SharedTransitionScope.ItemsScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    taskId: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ItemsViewModel = koinViewModel(parameters = {
        parametersOf(taskId)
    })
) {
    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is ItemsSideEffects.NavigateBack -> {
                appState.popUp()
            }
        }
    }

    ItemsScreenContent(
        state = state,
        onBackClick = viewModel::navigateBack,
        onNewCreateModalShow = viewModel::onNewItemCreateClick,
        animatedVisibilityScope = animatedVisibilityScope,
        onDismissNewCreateRequest = viewModel::onNewItemDismiss,
        onNewItemNameChange = viewModel::onNewItemNameChange,
        onNewItemUnitChange = viewModel::onNewItemUnitChange,
        onNewItemCountChange = viewModel::onNewItemCountChange,
        onNewItemPriceChange = viewModel::onNewItemPriceChange,
        onItemBoughtChange = viewModel::onItemBoughtChange,
        onDeleteItem = viewModel::onItemDelete,
        onEditItem = viewModel::onEditModalShow,
        onDismissEditModal = viewModel::onEditModalDismiss,
        onEditConfirm = viewModel::onEditConfirm,
        onItemsChangeStatusCall = viewModel::onItemsChangeStatusCall,
        onNewCreateConfirm = viewModel::onNewCreateConfirm
    )
}

@Composable
fun SharedTransitionScope.ItemsScreenContent(
    state: ItemsState,
    onBackClick: () -> Unit,
    onNewCreateModalShow: () -> Unit,
    onDismissNewCreateRequest: () -> Unit,
    onNewItemNameChange: (String) -> Unit,
    onNewItemCountChange: (Int) -> Unit,
    onNewItemUnitChange: (UnitType) -> Unit,
    onNewItemPriceChange: (Int) -> Unit,
    onNewCreateConfirm: () -> Unit,
    onItemBoughtChange: (Item) -> Unit,
    onEditItem: (Item) -> Unit,
    onDeleteItem: (Item) -> Unit,
    onDismissEditModal: () -> Unit,
    onEditConfirm: (Item) -> Unit,
    onItemsChangeStatusCall: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val scrollState = rememberLazyListState()
    val liquidState = rememberLiquidState()
    val hazeState = rememberHazeState()

    CreateItemModal(
        state = state,
        liquidState = liquidState,
        onDismissRequest = onDismissNewCreateRequest,
        onNewItemNameChange = onNewItemNameChange,
        onNewItemCountChange = onNewItemCountChange,
        onNewItemUnitChange = onNewItemUnitChange,
        onNewItemPriceChange = onNewItemPriceChange,
        onNewCreateConfirm = onNewCreateConfirm
    )

    EditItemModal(
        state = state,
        liquidState = liquidState,
        onDismissRequest = onDismissEditModal,
        onEditConfirm = onEditConfirm
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.details_back),
            contentDescription = null,
            modifier = Modifier
                .liquefiable(liquidState)
                .hazeSource(state = hazeState)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .liquefiable(liquidState)
                .fillMaxSize(),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {

            stickyHeader {
                Header(
                    state = state,
                    animatedVisibilityScope = animatedVisibilityScope,
                    hazeState = hazeState,
                    liquidState = liquidState,
                    onBackClick = onBackClick,
                    onItemsChangeStatusCall = onItemsChangeStatusCall
                )
            }

            items(items = state.task?.items ?: emptyList(), key = { it.uid }) { item ->
                ItemView(
                    modifier = Modifier
                        .animateItem()
                        .hazeSource(state = hazeState),
                    item = item,
                    liquidState = liquidState,
                    onItemBoughtChange = onItemBoughtChange,
                    onEditItem = onEditItem,
                    onDeleteItem = onDeleteItem
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


        AnimatedVisibility(
            modifier = Modifier
                .navigationBarsPadding()
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            enter = scaleIn(),
            exit = scaleOut(),
            visible = !scrollState.isScrollInProgress
        ) {
            ScaleButtonBox(
                modifier = Modifier.size(if (isPortrait()) dimens.large else dimens.regular),
                liquidState = liquidState,
                onClick = onNewCreateModalShow
            ) {
                Icon(
                    modifier = Modifier.size(dimens.medium4),
                    imageVector = Icons.Filled.Add,
                    contentDescription = "add",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}