package ru.asmelnikov.liquidshopper.presentation.tasks.components.edittaskmodal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.darkokoa.datetimewheelpicker.WheelDateTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ScrollDateTimePicker(
    modifier: Modifier = Modifier,
    startDateTime: LocalDateTime,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
    onDateChange: (LocalDateTime) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        WheelDateTimePicker(
            modifier = Modifier,
            size = DpSize(
                width = maxWidth,
                height = 168.dp
            ),
            startDateTime = startDateTime.toKotlinLocalDateTime(),
            yearsRange = IntRange(LocalDate.now().minusYears(25).year, LocalDate.now().plusYears(25).year),
            textStyle = MaterialTheme.typography.titleLarge,
            textColor = MaterialTheme.colorScheme.onBackground,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                shape = shape,
                border = null,
                color = color
            )
        ) { snappedDate ->
            onDateChange(snappedDate.toJavaLocalDateTime())
        }
    }
}