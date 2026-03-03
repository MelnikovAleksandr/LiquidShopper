package ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox
import ru.asmelnikov.liquidshopper.utils.datetime.toDisplay
import java.time.YearMonth

@Composable
fun MonthsCalendarTitle(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth,
    liquidState: LiquidState,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
    onMonthDialogShow: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ScaleButtonBox(
                modifier = Modifier.size(dimens.medium4),
                liquidState = liquidState,
                onClick = goToPrevious
            ) {
                Icon(
                    modifier = Modifier.size(dimens.medium3),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "prev",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(dimens.small3))
            ScaleButtonBox(
                modifier = Modifier.height(dimens.medium4)
                    .weight(1f),
                liquidState = liquidState,
                onClick = onMonthDialogShow
            ) {
                Text(
                    modifier = Modifier,
                    text = currentMonth.toDisplay(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(dimens.small3))
            ScaleButtonBox(
                modifier = Modifier.size(dimens.medium4),
                liquidState = liquidState,
                onClick = goToNext
            ) {
                Icon(
                    modifier = Modifier.size(dimens.medium3),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "next",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview
@Composable
private fun MonthsCalendarTitlePreview() {
    LiquidShopperTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MonthsCalendarTitle(
                currentMonth = YearMonth.now(),
                liquidState = rememberLiquidState(),
                goToPrevious = {},
                goToNext = {},
                onMonthDialogShow = {}
            )
        }
    }
}