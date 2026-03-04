package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.LiquidState
import ru.asmelnikov.liquidshopper.presentation.tasks.components.TaskDropDown
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import ru.asmelnikov.liquidshopper.utils.components.LiquidParams
import ru.asmelnikov.liquidshopper.utils.components.ScaleButtonRow

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
            ScaleButtonRow(
                liquidState = liquidState,
                liquidParams = liquidParams,
                onClick = {
                    onDismiss()
                    when (label) {
                        TaskDropDown.EDIT -> onEditClick()
                        TaskDropDown.DELETE -> onDeleteClick()
                    }
                }
            ) {
                Icon(
                    imageVector = label.image,
                    contentDescription = "icon",
                    modifier = Modifier.padding(dimens.small3).size(dimens.medium2),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    modifier = Modifier.padding(end = dimens.small3),
                    text = stringResource(label.strRes),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}