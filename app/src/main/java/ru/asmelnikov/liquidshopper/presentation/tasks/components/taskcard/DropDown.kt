package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid
import ru.asmelnikov.liquidshopper.presentation.tasks.components.LiquidParams
import ru.asmelnikov.liquidshopper.presentation.tasks.components.TaskDropDown

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
                    Text(
                        text = stringResource(label.strRes),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            )
        }
    }
}