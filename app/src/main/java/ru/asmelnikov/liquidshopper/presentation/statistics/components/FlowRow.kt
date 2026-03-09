package ru.asmelnikov.liquidshopper.presentation.statistics.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Statistic
import ru.asmelnikov.liquidshopper.presentation.theme.dimens

@Composable
fun FlowRowItems(
    items: Statistic,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.padding(horizontal = dimens.medium1),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.legendMapSet.toList().forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(item.first.color)
                    .padding(
                        horizontal = dimens.small2,
                        vertical = dimens.small3 / 2f
                    )
            ) {
                Image(
                    modifier = Modifier.size(dimens.medium1),
                    painter = painterResource(item.first.drawableRes),
                    contentDescription = null
                )

                Text(
                    text = "${stringResource(item.first.stringRes)} ${item.second} ${
                        stringResource(R.string.item_currency)
                    }",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}