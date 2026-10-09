package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.Constant

@Composable
fun SearchSimpleRowComponent(
    modifier: Modifier = Modifier,
    value: String,
    onClickSearch: () -> Unit
) = Row(
    modifier = modifier
        .clip(RoundedCornerShape(100.dp))
        .clickable { onClickSearch() }
        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        .padding(vertical = 8.dp, horizontal = 18.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Icon(
        tint = MaterialTheme.colorScheme.secondary,
        imageVector = CustomIcon.FILL_SEARCH,
        contentDescription = Constant.SEARCH_ICON
    )
    Text(
        color = MaterialTheme.colorScheme.outline,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        text = value
    )
}

/**
 * Search text field backed by a [TextFieldState]; the caller owns the state and reads the
 * typed text from it. Styled like [SearchSimpleRowComponent] with a primary outline.
 *
 * @param state holds the text and selection, use `rememberTextFieldState()` to create one.
 * @param placeholder hint shown while the field is empty.
 * @param onSearch invoked when the keyboard search action is pressed.
 */
@Composable
fun SearchTextFieldComponent(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onSearch: () -> Unit = {}
) {
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    )
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val accentColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }

    BasicTextField(
        state = state,
        modifier = modifier,
        interactionSource = interactionSource,
        textStyle = textStyle,
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onKeyboardAction = KeyboardActionHandler { performDefaultAction ->
            onSearch()
            performDefaultAction()
            focusManager.clearFocus()
        },
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .border(
                        width = 1.5.dp,
                        color = if (isFocused) accentColor else Color.Transparent,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    tint = if (isFocused) accentColor else MaterialTheme.colorScheme.secondary,
                    imageVector = CustomIcon.FILL_SEARCH,
                    contentDescription = Constant.SEARCH_ICON
                )
                Box(modifier = Modifier.weight(1F)) {
                    if (state.text.isEmpty()) {
                        Text(
                            color = MaterialTheme.colorScheme.outline,
                            style = textStyle,
                            text = placeholder
                        )
                    }
                    innerTextField()
                }
                if (state.text.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { state.clearText() }
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.outline,
                            imageVector = CustomIcon.FILL_CLOSE,
                            contentDescription = Constant.CLEAR_ICON
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchSimpleRowComponentPreview() {
    SearchSimpleRowComponent(
        value = "Search",
        onClickSearch = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldComponentEmptyPreview() {
    SearchTextFieldComponent(
        state = rememberTextFieldState(),
        placeholder = "Search"
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldComponentFilledPreview() {
    SearchTextFieldComponent(
        state = rememberTextFieldState(initialText = "Ble"),
        placeholder = "Search"
    )
}
