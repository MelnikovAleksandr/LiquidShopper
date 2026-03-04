package ru.asmelnikov.liquidshopper.presentation.tasks.components.taskmodal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.darkokoa.datetimewheelpicker.WheelTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalTime

@Composable
fun ScrollTimePicker(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
    onDateChange: (LocalTime) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        WheelTimePicker(
            modifier = Modifier,
            startTime = LocalTime.now().toKotlinLocalTime(),
            size = DpSize(
                width = maxWidth,
                height = 168.dp
            ),
            textStyle = MaterialTheme.typography.titleLarge,
            textColor = MaterialTheme.colorScheme.onBackground,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                shape = shape,
                border = null,
                color = color
            )
        ) { snappedDate ->
            onDateChange(snappedDate.toJavaLocalTime())
        }
    }
}