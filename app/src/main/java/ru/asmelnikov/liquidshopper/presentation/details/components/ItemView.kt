package ru.asmelnikov.liquidshopper.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.launch
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.taskItemsMock
import ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard.DropDown
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.LiquidParams
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonColumn
import ru.asmelnikov.liquidshopper.utils.components.navigationBarsPaddingIfLandscape

@Composable
fun ItemView(
    modifier: Modifier = Modifier,
    item: Item,
    liquidState: LiquidState,
    onItemBoughtChange: (Item) -> Unit,
    onEditItem: (Item) -> Unit,
    onDeleteItem: (Item) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val swipeState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        positionalThreshold = { totalDistance -> totalDistance / 2f }
    )

    SwipeToDismissBox(
        state = swipeState,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.small1)),
        onDismiss = { _ ->
            onDeleteItem(item)
            scope.launch {
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        },
        backgroundContent = {},
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = dimens.small1)
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimens.small1))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    modifier = Modifier.padding(dimens.small3),
                    checked = item.bought,
                    onCheckedChange = {
                        onItemBoughtChange(item.copy(bought = it))
                    },
                    thumbContent = if (item.bought) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )

                Column(
                    modifier = Modifier
                        .padding(dimens.small1)
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(dimens.extraSmall1)
                            .basicMarquee(Int.MAX_VALUE),
                        text = item.itemName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 2
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimens.extraSmall1)
                    ) {
                        Text(
                            text = "${item.count} ${stringResource(item.units.stringRes)}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (item.price > 0) {
                            Spacer(modifier = Modifier.width(dimens.small3))
                            Text(
                                text = "${item.price} ${stringResource(R.string.item_currency)}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                ScaleButtonColumn(
                    modifier = Modifier
                        .navigationBarsPaddingIfLandscape()
                        .padding(end = dimens.small3)
                        .size(dimens.medium4),
                    liquidState = liquidState,
                    onClick = {
                        expanded = true
                    }
                ) {
                    DropDown(
                        modifier = Modifier,
                        liquidState = liquidState,
                        liquidParams = LiquidParams().copy(dispersion = 1f),
                        expanded = expanded,
                        onDismiss = {
                            expanded = false
                        },
                        onEditClick = {
                            onEditItem(item)
                        },
                        onDeleteClick = {
                            onDeleteItem(item)
                        }
                    )

                    Icon(
                        modifier = Modifier.size(dimens.medium3),
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "menu",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }


            }
        }
    }
}

@Preview
@Composable
private fun ItemViewPreview1() {
    LiquidShopperTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {

            var item by remember {
                mutableStateOf(taskItemsMock.first())
            }

            ItemView(
                item = item,
                liquidState = rememberLiquidState(),
                onItemBoughtChange = { itemFrom ->
                    item = itemFrom
                },
                onEditItem = {},
                onDeleteItem = {}
            )

        }
    }
}

@Preview
@Composable
private fun ItemViewPreview2() {
    LiquidShopperTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {

            var item by remember {
                mutableStateOf(taskItemsMock.first().copy(bought = true))
            }

            ItemView(
                item = item,
                liquidState = rememberLiquidState(),
                onItemBoughtChange = { itemFrom ->
                    item = itemFrom
                },
                onEditItem = {},
                onDeleteItem = {}
            )

        }
    }
}