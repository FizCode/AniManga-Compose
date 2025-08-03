package dev.fizcode.designsystem.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.util.Constant

@Composable
fun ErrorCardComponent(
    modifier: Modifier = Modifier,
    imgResource: Int = Constant.IMG_ERROR_404_PATH,
    errorCode: String,
    errorDesc: String
) = Column(
    modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.errorContainer)
        .padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp),
        painter = painterResource(imgResource),
        contentDescription = Constant.IMG_ERROR_DESC
    )
    Text(
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        text = errorCode
    )
    Text(
        color = MaterialTheme.colorScheme.onErrorContainer,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        text = errorDesc
    )
}

@Composable
fun ErrorCardWithButtonComponent(
    modifier: Modifier = Modifier,
    imgResource: Int = Constant.IMG_EMPTY_PATH,
    errorCode: String,
    errorDesc: String,
    onErrorButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            painter = painterResource(imgResource),
            contentDescription = Constant.IMG_ERROR_DESC
        )
        Text(
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = errorCode
        )
        Text(
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = errorDesc
        )
        Spacer(Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            onClick = onErrorButtonClick
        ) {
            Text("Reload")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ErrorComponentPreview() = Column {
    ErrorCardComponent(
        errorCode = "404",
        errorDesc = "Page not found."
    )
    ErrorCardWithButtonComponent(
        errorCode = "501",
        errorDesc = "Server error.",
        onErrorButtonClick = {}
    )
}

