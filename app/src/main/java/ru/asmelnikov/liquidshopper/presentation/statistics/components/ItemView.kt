package ru.asmelnikov.liquidshopper.presentation.statistics.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.taskItemsMock
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox

@Composable
fun ItemView(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    item: Item
) {
    Box(
        modifier = modifier
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
            ScaleButtonBox(
                modifier = Modifier.padding(start = dimens.small1).size(dimens.medium4),
                liquidState = liquidState,
                enabled = false,
                onClick = {}
            ) {

                Box(
                    modifier = Modifier
                        .size(dimens.medium3)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(dimens.medium2),
                        imageVector = if (item.bought) Icons.Sharp.Check else Icons.Sharp.Clear,
                        contentDescription = "",
                        tint = if (item.bought) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

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
        }
    }
}

@Preview
@Composable
private fun ItemViewPreview() {
    LiquidShopperTheme(darkTheme = true) {
        val liquidState = rememberLiquidState()
        Box(
            modifier = Modifier
                .size(400.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Image(
                painter = painterResource(R.drawable.background_pattern_1),
                contentDescription = null,
                modifier = Modifier
                    .liquefiable(liquidState)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentScale = ContentScale.Crop
            )

            ItemView(
                item = taskItemsMock.first(),
                liquidState = liquidState
            )
        }
    }
}