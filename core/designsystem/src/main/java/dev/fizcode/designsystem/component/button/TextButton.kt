package dev.fizcode.designsystem.component.button

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * Plain Material text button showing a single [text] label.
 *
 * @param modifier modifier for the button (currently not applied to the underlying button).
 * @param text label displayed inside the button.
 * @param onClickButton invoked when the button is clicked.
 */
@Composable
fun SimpleTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClickButton: () -> Unit
) {
    TextButton(
        onClick = onClickButton
    ) {
        Text(
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleTextButtonPreview() {
    SimpleTextButton(text = "See All", onClickButton = {})
}
