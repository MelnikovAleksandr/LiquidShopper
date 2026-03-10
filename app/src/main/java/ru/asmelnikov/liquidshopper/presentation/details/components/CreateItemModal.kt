package ru.asmelnikov.liquidshopper.presentation.details.components

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
import androidx.compose.foundation.layout.width
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
import ru.asmelnikov.liquidshopper.domain.models.UnitType
import ru.asmelnikov.liquidshopper.presentation.details.viewmodel.ItemsState
import ru.asmelnikov.liquidshopper.presentation.tasks.components.newtaskmodal.TextFieldCustom
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.EnumDropDown
import ru.asmelnikov.liquidshopper.utils.components.LiquidParams
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemModal(
    modifier: Modifier = Modifier,
    state: ItemsState,
    newItemName: String,
    liquidState: LiquidState,
    liquidParams: LiquidParams = LiquidParams(),
    onDismissRequest: () -> Unit,
    onNewItemNameChange: (String) -> Unit,
    onNewItemCountChange: (Int) -> Unit,
    onNewItemUnitChange: (UnitType) -> Unit,
    onNewItemPriceChange: (Int) -> Unit,
    onNewCreateConfirm: () -> Unit
) {

    if (state.isNewItemModalShow) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
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
                    .fillMaxWidth(),
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(dimens.small3))

                    TextFieldCustom(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.small1),
                        title = newItemName,
                        maxInputLength = 75,
                        onTitleChange = onNewItemNameChange,
                        isEmptyError = state.emptyNewItemNameError,
                        emptyPlaceholder = stringResource(R.string.item_placeholder),
                        errorString = stringResource(R.string.item_placeholder),
                        focusManager = focusManager,
                        keyboardController = keyboardController
                    )

                    Spacer(modifier = Modifier.height(dimens.small3))

                    CountSlider(
                        modifier = Modifier,
                        liquidState = liquidState,
                        liquidParams = liquidParams,
                        count = state.newItemCount,
                        onCountChange = onNewItemCountChange
                    )

                    Spacer(modifier = Modifier.height(dimens.small3))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EnumDropDown(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = dimens.small1),
                            items = UnitType.entries,
                            onItemChanged = {
                                onNewItemUnitChange(it as UnitType)
                            },
                            selectedItem = state.newItemUnit,
                            focusManager = focusManager,
                            keyboardController = keyboardController
                        )
                        Spacer(modifier = Modifier.width(dimens.small1))

                        TextFieldCustom(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = dimens.small1),
                            title = state.newItemPrice.toString(),
                            maxInputLength = 8,
                            onTitleChange = {
                                onNewItemPriceChange(it.toIntOrNull() ?: 0)
                            },
                            isEmptyError = false,
                            isNumericOnly = true,
                            emptyPlaceholder = stringResource(R.string.item_price),
                            errorString = stringResource(R.string.empty),
                            focusManager = focusManager,
                            keyboardController = keyboardController
                        )

                    }

                    Spacer(modifier = Modifier.height(dimens.small3))

                    ScaleButtonRow(
                        modifier = Modifier
                            .padding(dimens.small1)
                            .navigationBarsPadding(),
                        liquidState = liquidState,
                        liquidParams = liquidParams,
                        onClick = onNewCreateConfirm
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
                            text = stringResource(R.string.item_create),
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
}