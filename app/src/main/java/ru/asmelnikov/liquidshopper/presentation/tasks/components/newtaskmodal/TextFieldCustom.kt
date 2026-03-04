package ru.asmelnikov.liquidshopper.presentation.tasks.components.newtaskmodal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme

@Composable
fun TextFieldCustom(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    maxInputLength: Int = 100,
    emptyPlaceholder: String,
    errorString: String,
    isEmptyError: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    isNumericOnly: Boolean = false
) {
    TextField(
        modifier = modifier.clip(MaterialTheme.shapes.medium),
        value = title,
        onValueChange = { newValue ->
            val filteredValue = if (isNumericOnly) {
                newValue.filter { it.isDigit() }
            } else {
                newValue
            }
            if (filteredValue.length <= maxInputLength) {
                onTitleChange(filteredValue)
            }
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            color = MaterialTheme.colorScheme.onSecondary
        ),
        label = {
            AnimatedVisibility(visible = title.length >= maxInputLength) {
                Text(
                    text = stringResource(R.string.task_title_error, maxInputLength),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onError,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            AnimatedVisibility(visible = !isEmptyError && title.length < maxInputLength) {
                Text(
                    text = emptyPlaceholder,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            AnimatedVisibility(visible = isEmptyError) {
                Text(
                    text = errorString,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        keyboardOptions = if (isNumericOnly) {
            keyboardOptions.copy(
                keyboardType = KeyboardType.Number
            )
        } else {
            keyboardOptions
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Preview
@Composable
private fun TextFieldPreview() {
    LiquidShopperTheme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            val focusManager = LocalFocusManager.current
            TextFieldCustom(
                title = "",
                onTitleChange = {},
                maxInputLength = 6793,
                isEmptyError = true,
                emptyPlaceholder = "Введите название списка",
                errorString = "Введите название списка",
                focusManager = focusManager,
                keyboardController = null
            )
        }
    }
}