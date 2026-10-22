package dev.fizcode.designsystem.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.animation.AnimangaGeneralErrorIllustration
import dev.fizcode.designsystem.util.Constant

/**
 * Error state without a background: the animated general error illustration in the theme's error color,
 * an error code and its description, centered in the space the parent gives it.
 *
 * @param modifier modifier applied to the root column.
 * @param errorCode headline of the error, e.g. an HTTP status code.
 * @param errorDesc supporting message explaining the error.
 * @see ErrorCardWithButtonComponent for a variant with a retry action.
 */
@Composable
fun ErrorCardComponent(
    modifier: Modifier = Modifier,
    errorCode: String,
    errorDesc: String
) = Column(
    modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    AnimangaGeneralErrorIllustration(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .aspectRatio(1f),
        contentDescription = Constant.IMG_ERROR_DESC,
        accentColor = MaterialTheme.colorScheme.error,
        onAccentColor = MaterialTheme.colorScheme.onError
    )
    Text(
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        text = errorCode
    )
    Text(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        text = errorDesc
    )
}

/**
 * Same as [ErrorCardComponent] with a full-width "Reload" button below the description.
 *
 * @param modifier modifier applied to the root column.
 * @param errorCode headline of the error, e.g. an HTTP status code.
 * @param errorDesc supporting message explaining the error.
 * @param onErrorButtonClick invoked when the reload button is clicked.
 */
@Composable
fun ErrorCardWithButtonComponent(
    modifier: Modifier = Modifier,
    errorCode: String,
    errorDesc: String,
    onErrorButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimangaGeneralErrorIllustration(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .aspectRatio(1f),
            contentDescription = Constant.IMG_ERROR_DESC,
            accentColor = MaterialTheme.colorScheme.error,
            onAccentColor = MaterialTheme.colorScheme.onError
        )
        Text(
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = errorCode
        )
        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = errorDesc
        )
        Spacer(Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            onClick = onErrorButtonClick
        ) {
            Text("Reload")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorCardComponentPreview() = ErrorCardComponent(
    errorCode = "404",
    errorDesc = "Page not found."
)

@Preview(showBackground = true)
@Composable
private fun ErrorCardWithButtonComponentPreview() = ErrorCardWithButtonComponent(
    errorCode = "501",
    errorDesc = "Server error.",
    onErrorButtonClick = {}
)
