package dev.fizcode.mediadetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetails.util.Constant

@Composable
internal fun PopUpImage(
    imageUrl: String,
    onDismiss: () -> Unit
) = Popup(
    alignment = Alignment.Center,
    onDismissRequest = onDismiss,
    properties = PopupProperties(focusable = true)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85F))
            .clickable { onDismiss() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(shimmerBrush()),
            contentScale = ContentScale.Fit
        )
        TextButton(
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = {}
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.surfaceBright,
                imageVector = CustomIcon.ROUND_DOWNLOAD,
                contentDescription = Constant.DOWNLOAD_ICON
            )
            Spacer(Modifier.width(8.dp))
            Text(
                color = MaterialTheme.colorScheme.surfaceBright,
                text = Constant.DOWNLOAD
            )
        }
    }
}
