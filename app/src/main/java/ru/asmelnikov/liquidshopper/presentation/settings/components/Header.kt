package ru.asmelnikov.liquidshopper.presentation.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonBox
import ru.asmelnikov.liquidshopper.utils.components.navigationBarsPaddingIfLandscape

@Composable
fun Header(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    liquidState: LiquidState,
    onBackClick: () -> Unit
) {

    Column(
        modifier = modifier
            .hazeEffect(state = hazeState, style = HazeStyle.Unspecified)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(dimens.small1)
                .navigationBarsPaddingIfLandscape(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScaleButtonBox(
                modifier = Modifier.size(dimens.medium5),
                liquidState = liquidState,
                onClick = onBackClick
            ) {
                Icon(
                    modifier = Modifier.size(dimens.medium4),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "prev",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(dimens.small3))
            ScaleButtonBox(
                modifier = Modifier
                    .height(dimens.medium5)
                    .weight(1f),
                liquidState = liquidState,
                enabled = false,
                onClick = {}
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = dimens.small3),
                    text = stringResource(R.string.settings_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.width(dimens.small3))
            ScaleButtonBox(
                modifier = Modifier.size(dimens.medium5),
                liquidState = liquidState,
                enabled = false,
                onClick = {}
            ) {
                Icon(
                    modifier = Modifier
                        .size(dimens.medium4),
                    painter = painterResource(R.drawable.settings_colored_icon),
                    contentDescription = "type",
                    tint = Color.Unspecified
                )
            }
        }
    }

}

@Preview
@Composable
private fun HeaderPreview() {
    LiquidShopperTheme {
        val liquidState = rememberLiquidState()
        val hazeState = rememberHazeState()
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
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentScale = ContentScale.Crop
            )

            Header(
                modifier = Modifier.align(Alignment.TopCenter),
                liquidState = liquidState,
                hazeState = hazeState,
                onBackClick = {}
            )
        }
    }
}