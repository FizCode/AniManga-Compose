package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.animation.AnimangaGatewayTimeoutIllustration
import dev.fizcode.designsystem.util.Constant

/**
 * Full-width animated gateway timeout illustration with a retry button, shown in place of content
 * that failed to load.
 *
 * @param onRetry called when the retry button is clicked.
 * @param modifier modifier applied to the root column.
 * @param retryText label of the retry button.
 */
@Composable
fun ErrorWithRetry(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    retryText: String = Constant.RETRY
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp, vertical = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    AnimangaGatewayTimeoutIllustration(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )

    Button(onClick = onRetry) {
        Text(text = retryText)
    }
}

/**
 * Small inline notice for a section whose data is missing, with an optional retry.
 *
 * @param message text explaining what is missing.
 * @param modifier modifier applied to the root column.
 * @param onRetry called when the retry button is clicked, the button is hidden when null.
 * @param retryText label of the retry button.
 */
@Composable
fun UnavailableNotice(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    retryText: String = Constant.RETRY
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
    if (onRetry != null) {
        TextButton(onClick = onRetry) {
            Text(text = retryText)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorWithRetryPreview() {
    ErrorWithRetry(onRetry = {})
}

@Composable
@Preview(showBackground = true)
private fun UnavailableNoticePreview() {
    UnavailableNotice(message = "Cast and staff are unavailable right now.")
}

@Composable
@Preview(showBackground = true)
private fun UnavailableNoticeWithRetryPreview() {
    UnavailableNotice(
        message = "Cast and staff are unavailable right now.",
        onRetry = {}
    )
}
