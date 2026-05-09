package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import ru.asmelnikov.liquidshopper.presentation.tasks.components.TaskDropDown
import ru.asmelnikov.liquidshopper.presentation.theme.dimens

@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    liquidState: Backdrop,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val sheetShape = MaterialTheme.shapes.extraLarge
    DropdownMenu(
        modifier = modifier.drawBackdrop(
            backdrop = liquidState,
            shape = { sheetShape },
            effects = {
                vibrancy()
                blur(4f.dp.toPx())
                lens(24f.dp.toPx(), 48f.dp.toPx(), true)
            }
        ),
        containerColor = Color.Transparent,
        shadowElevation = 0.dp,
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        TaskDropDown.entries.forEach { label ->
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
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
                },
                onClick = {
                    onDismiss()
                    when (label) {
                        TaskDropDown.EDIT -> onEditClick()
                        TaskDropDown.DELETE -> onDeleteClick()
                    }
                }
            )
        }
    }
}