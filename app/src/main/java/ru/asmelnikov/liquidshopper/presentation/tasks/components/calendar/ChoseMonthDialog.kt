package ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.utils.components.liquid.LiquidButtonBox
import ru.asmelnikov.liquidshopper.presentation.tasks.viewmodel.TasksState
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import java.time.LocalDate

@Composable
fun ChoseMonthDialog(
    modifier: Modifier = Modifier,
    state: TasksState,
    liquidState: Backdrop,
    onDismissRequest: () -> Unit,
    chosenMonth: (LocalDate) -> Unit
) {
    if (state.isShowSelectMonthDialog) {

        val dialogSheetBackdrop = rememberLayerBackdrop()
        val dialogShape = MaterialTheme.shapes.extraLarge
        var chosenDate by remember {
            mutableStateOf(LocalDate.now())
        }

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(),
        ) {

            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surface, dialogShape)
                    .drawBackdrop(
                        backdrop = liquidState,
                        shape = { dialogShape },
                        effects = {
                            vibrancy()
                            blur(4f.dp.toPx())
                            lens(24f.dp.toPx(), 48f.dp.toPx(), true)
                        },
                        exportedBackdrop = dialogSheetBackdrop
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ScrollDatePicker(
                    modifier = Modifier.fillMaxWidth().padding(dimens.small3)
                ) { date ->
                    chosenDate = date
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LiquidButtonBox(
                        onClick = onDismissRequest,
                        backdrop = dialogSheetBackdrop,
                        modifier = Modifier.weight(1f).padding(start = dimens.small3),
                        content = {
                            Text(
                                modifier = Modifier.padding(dimens.small3),
                                text = stringResource(R.string.date_dismiss),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(dimens.small3))
                    LiquidButtonBox(
                        onClick = {
                            chosenMonth(chosenDate)
                            onDismissRequest()
                        },
                        backdrop = dialogSheetBackdrop,
                        modifier = Modifier.weight(1f).padding(end = dimens.small3),
                        content = {
                            Text(
                                modifier = Modifier.padding(dimens.small3),
                                text = stringResource(R.string.date_confirm),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(dimens.small3))
            }
        }
    }
}