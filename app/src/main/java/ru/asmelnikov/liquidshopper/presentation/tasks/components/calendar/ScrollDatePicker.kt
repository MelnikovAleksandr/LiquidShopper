package ru.asmelnikov.liquidshopper.presentation.tasks.components.calendar

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalDate

@Composable
fun ScrollDatePicker(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
    onDateChange: (LocalDate) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        WheelDatePicker(
            modifier = Modifier,
            size = DpSize(
                width = maxWidth,
                height = 168.dp
            ),
            yearsRange = IntRange(1999, 2050),
            textStyle = MaterialTheme.typography.titleLarge,
            textColor = MaterialTheme.colorScheme.onBackground,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                shape = shape,
                border = null,
                color = color
            )
        ) { snappedDate ->
            onDateChange(snappedDate.toJavaLocalDate())
        }
    }
}