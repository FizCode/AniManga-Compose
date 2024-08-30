package dev.fizcode.designsystem.component.button

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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