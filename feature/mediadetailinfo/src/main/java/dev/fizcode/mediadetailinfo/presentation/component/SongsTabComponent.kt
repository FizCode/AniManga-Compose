package dev.fizcode.mediadetailinfo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.mediadetailinfo.model.AnimeThemes
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun SongsTabComponent(
    songThemes: AnimeThemes
) {

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SongContent(title = "Opening Theme", songs = songThemes.openingTheme)
        SongContent(title = "Ending Theme", songs = songThemes.endingTheme)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SongContent(
    title: String,
    songs: List<String>
) = Column(
    modifier = Modifier.padding(top = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        text = title
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        songs.forEachIndexed { _, data ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SongsTabPreview() {

    val singleContent =
        persistentListOf("\"Scar (スカー)\" by Tatsuya Kitani (キタニタツヤ) (eps 2-13)")
    val multipleContents = persistentListOf(
        "\"Rapport\" by Tatsuya Kitani (キタニタツヤ) (eps 1)",
        "\"Saihate (最果て)\" by SennaRin (eps 2-12)",
        "\"Number One - Bankai\" by Hazel Fernandes (eps 13)"
    )
    SongsTabComponent(
        songThemes = AnimeThemes(
            openingTheme = singleContent,
            endingTheme = multipleContents
        )
    )
}