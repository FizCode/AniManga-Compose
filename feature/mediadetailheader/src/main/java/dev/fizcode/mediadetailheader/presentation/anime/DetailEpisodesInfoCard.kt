package dev.fizcode.mediadetailheader.presentation.anime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun DetailEpisodesInfoCard(
    modifier: Modifier = Modifier,
    releaseInfo: String,
    duration: String,
) = Column(
    /**
     * This custom modifier is to applying global positioning logic to triggering UI changes based
     * on scroll.
     */
    modifier = modifier
        .padding(horizontal = 16.dp)
        .padding(top = 8.dp)
        .background(MaterialTheme.colorScheme.surfaceContainerLow),
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        text = releaseInfo
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        text = duration
    )
}
