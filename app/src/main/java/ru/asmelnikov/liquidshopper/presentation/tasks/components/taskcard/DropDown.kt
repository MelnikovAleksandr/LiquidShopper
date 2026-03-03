package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid
import ru.asmelnikov.liquidshopper.utils.components.LiquidParams
import ru.asmelnikov.liquidshopper.presentation.tasks.components.TaskDropDown
import ru.asmelnikov.liquidshopper.presentation.theme.dimens

@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    liquidState: LiquidState,
    liquidParams: LiquidParams,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        containerColor = Color.Transparent,
        shadowElevation = 0.dp,
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        TaskDropDown.entries.forEach { label ->
            DropdownMenuItem(
                modifier = Modifier.liquid(liquidState, liquidParams()),
                onClick = {
                    onDismiss()
                    when (label) {
                        TaskDropDown.EDIT -> {
                            onEditClick()
                        }
                        TaskDropDown.DELETE -> {
                            onDeleteClick()
                        }
                    }
                },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = label.image,
                            contentDescription = "icon",
                            modifier = Modifier.size(dimens.medium2),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(dimens.small3))
                        Text(
                            text = stringResource(label.strRes),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            )
        }
    }
}