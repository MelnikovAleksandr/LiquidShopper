package ru.asmelnikov.liquidshopper.presentation.tasks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox

@Composable
fun MultiButton(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    onCreateClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onStatisticsClick: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .padding(dimens.medium2)
            .navigationBarsPadding()
    ) {
        val (settings, statistics, create) = createRefs()

        ScaleButtonBox(
            modifier = Modifier
                .constrainAs(create) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
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

        ScaleButtonBox(
            modifier = Modifier
                .constrainAs(settings) {
                    bottom.linkTo(create.top)
                    end.linkTo(parent.end)
                }
                .size(dimens.medium3),
            liquidState = liquidState,
            onClick = onSettingsClick
        ) {
            Icon(
                modifier = Modifier
                    .size(dimens.medium2),
                painter = painterResource(R.drawable.settings_icon),
                contentDescription = "settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        ScaleButtonBox(
            modifier = Modifier
                .constrainAs(statistics) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(create.start)
                }
                .size(dimens.medium3),
            liquidState = liquidState,
            onClick = onStatisticsClick
        ) {
            Icon(
                modifier = Modifier
                    .size(dimens.medium2),
                painter = painterResource(R.drawable.statistics_icon),
                contentDescription = "statistics",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun MultiButtonPreview() {
    LiquidShopperTheme {
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

            MultiButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                liquidState = liquidState,
                onCreateClick = {},
                onSettingsClick = {},
                onStatisticsClick = {}
            )
        }
    }
}