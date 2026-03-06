package ru.asmelnikov.liquidshopper.utils.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.domain.models.UnitType
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme
import ru.asmelnikov.liquidshopper.presentation.theme.dimens

@Composable
fun EnumDropDown(
    modifier: Modifier = Modifier,
    items: List<Enum<*>>,
    onItemChanged: (Enum<*>) -> Unit,
    selectedItem: Enum<*>,
    withIcons: Boolean = false,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {

    var expanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "")

    Column(modifier = modifier) {
        TextField(
            value = stringResource(id = enumTextHelper(selectedItem)),
            onValueChange = {},
            modifier = Modifier
                .heightIn(max = 168.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    expanded = !expanded
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                color = MaterialTheme.colorScheme.onSecondary
            ),
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "icon",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.rotate(rotationAngle)
                )
            },
            leadingIcon = {
                if (withIcons) {
                    Icon(
                        painter = painterResource(id = enumDrawableHelper(selectedItem)),
                        contentDescription = "icon",
                        modifier = Modifier.size(dimens.medium3),
                        tint = Color.Unspecified
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        DropdownMenu(
            modifier = Modifier,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onItemChanged(label)
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (withIcons) {
                                Icon(
                                    painter = painterResource(id = enumDrawableHelper(label)),
                                    contentDescription = "icon",
                                    modifier = Modifier.size(dimens.medium2),
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.width(dimens.small3))
                            }
                            Text(
                                text = stringResource(id = enumTextHelper(label)),
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onBackground,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )
            }
        }
    }
}

fun enumTextHelper(enum: Enum<*>?): Int {
    return when (enum) {
        is TaskTypes -> enum.stringRes
        is UnitType -> enum.stringRes
        else -> R.string.empty
    }
}

fun enumDrawableHelper(enum: Enum<*>?): Int {
    return when (enum) {
        is TaskTypes -> enum.drawableRes
        else -> R.drawable.other_items_ic
    }
}

@Preview
@Composable
private fun EnumDropDownPreview() {
    LiquidShopperTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            val focusManager = LocalFocusManager.current
            EnumDropDown(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = dimens.small1),
                items = TaskTypes.entries,
                withIcons = true,
                onItemChanged = {},
                selectedItem = TaskTypes.GARDEN,
                focusManager = focusManager,
                keyboardController = null
            )
        }
    }
}