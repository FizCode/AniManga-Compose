package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon

@Composable
internal fun SongsTabComponent() {
    val singleContent = listOf("\"Scar (スカー)\" by Tatsuya Kitani (キタニタツヤ) (eps 2-13)")
    val multipleContents = listOf(
        "\"Rapport\" by Tatsuya Kitani (キタニタツヤ) (eps 1)",
        "\"Saihate (最果て)\" by SennaRin (eps 2-12)",
        "\"Number One - Bankai\" by Hazel Fernandes (eps 13)"
    )


    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SongContent(title = "Opening Theme", songs = singleContent)
        SongContent(title = "Ending Theme", songs = multipleContents)
    }
}

@Composable
private fun SongContent(
    title: String,
    songs: List<String>
) = Column {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        text = title
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        songs.forEachIndexed { index, data ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = CustomIcon.ROUND_PLAY_CIRCLE,
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (songs.size > 1) "${index + 1}: $data" else data
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SongsTabPreview() {
    SongsTabComponent()
}