package dev.fizcode.mediadetails.presentation.header.anime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.chip.GenreChip
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun GenreChips(genre: List<String>) = Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    LazyRow(
        modifier = Modifier.wrapContentWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = genre) { _, item ->
            GenreChip(item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreChipsPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        GenreChips(genre = listOf("Action", "Adventure", "Fantasy"))
    }
}
