package ru.asmelnikov.liquidshopper.presentation.statistics.components.picker

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import ru.asmelnikov.liquidshopper.presentation.theme.dimens
import java.time.LocalDate

fun Modifier.clickable(
    enabled: Boolean = true,
    showRipple: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = if (showRipple) LocalIndication.current else null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}

fun Modifier.backgroundHighlight(
    day: CalendarDay,
    today: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?,
    selectionColor: Color,
    continuousSelectionColor: Color,
    textColor: (Color) -> Unit,
): Modifier = composed {
    val padding = dimens.extraSmall1
    when (day.position) {
        DayPosition.MonthDate -> {
            when {
                startDate == day.date && endDate == null -> {
                    textColor(MaterialTheme.colorScheme.background)
                    padding(padding)
                        .background(color = selectionColor, shape = CircleShape)
                }

                day.date == startDate -> {
                    textColor(MaterialTheme.colorScheme.background)
                    padding(vertical = padding)
                        .background(
                            color = continuousSelectionColor,
                            shape = HalfSizeShape(clipStart = true),
                        )
                        .padding(horizontal = padding)
                        .background(color = selectionColor, shape = CircleShape)
                }

                day.date == endDate -> {
                    textColor(MaterialTheme.colorScheme.background)
                    padding(vertical = padding)
                        .background(
                            color = continuousSelectionColor,
                            shape = HalfSizeShape(clipStart = false),
                        )
                        .padding(horizontal = padding)
                        .background(color = selectionColor, shape = CircleShape)
                }

                startDate != null && endDate != null &&
                        (day.date > startDate && day.date < endDate) -> {
                    textColor(MaterialTheme.colorScheme.background)
                    padding(vertical = padding)
                        .background(color = continuousSelectionColor)
                }

                day.date == today -> {
                    textColor(MaterialTheme.colorScheme.onBackground)
                    padding(padding)
                        .border(
                            width = dimens.borderSize,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                }

                else -> {
                    textColor(MaterialTheme.colorScheme.onBackground)
                    this
                }
            }
        }

        DayPosition.InDate -> {
            if (startDate != null && endDate != null &&
                isInDateBetweenSelection(day.date, startDate, endDate)
            ) {
                textColor(MaterialTheme.colorScheme.background)
                padding(vertical = padding)
                    .background(color = continuousSelectionColor)
            } else {
                textColor(Color.Transparent)
                this
            }
        }

        DayPosition.OutDate -> {
            if (startDate != null && endDate != null &&
                isOutDateBetweenSelection(day.date, startDate, endDate)
            ) {
                textColor(MaterialTheme.colorScheme.onBackground)
                padding(vertical = padding)
                    .background(color = continuousSelectionColor)
            } else {
                textColor(Color.Transparent)
                this
            }
        }
    }
}