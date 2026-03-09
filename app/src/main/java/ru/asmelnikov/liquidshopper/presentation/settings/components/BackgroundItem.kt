package ru.asmelnikov.liquidshopper.presentation.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.domain.models.BackgroundImage
import ru.asmelnikov.liquidshopper.domain.models.Screens
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens

@Composable
fun BackgroundItem(
    modifier: Modifier = Modifier,
    background: Background,
    onBackChange: (Background) -> Unit
) {

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }


    Box(
        modifier = modifier
            .padding(horizontal = dimens.small1)
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.small1))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    )
                )
            )
            .clickable {
                isContextMenuVisible = !isContextMenuVisible
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                modifier = Modifier
                    .padding(dimens.small1),
                text = stringResource(background.screen.stringRes),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Box(
                modifier = Modifier
                    .padding(dimens.small1)
                    .background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium)
                    .size(dimens.regular)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(background.data.drawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = {
                isContextMenuVisible = false
            }
        ) {
            BackgroundImage.entries.forEach { back ->
                DropdownMenuItem(
                    onClick = {
                        onBackChange(background.copy(data = back))
                        isContextMenuVisible = false
                    },
                    text = {
                        Box(
                            modifier = Modifier
                                .padding(dimens.extraSmall1)
                                .width(dimens.large)
                                .height(dimens.medium4)
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize(),
                                painter = painterResource(back.drawableRes),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun BackgroundItemPreview() {
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

            BackgroundItem(
                modifier = Modifier.align(Alignment.Center),
                background = Background(
                    screen = Screens.MAIN,
                    data = BackgroundImage.BACKGROUND_PATTERN_3
                ),
                onBackChange = {}
            )
        }
    }
}