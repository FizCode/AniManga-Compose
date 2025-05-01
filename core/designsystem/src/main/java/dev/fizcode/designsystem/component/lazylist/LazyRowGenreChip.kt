package dev.fizcode.designsystem.component.lazylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.chip.GenreChip

@Composable
fun LazyRowGenreChip(
    genre: List<String>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(
            items = genre
        ) { _, item ->
            GenreChip(genre = item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LazyRowChipPreview() {
    LazyRowGenreChip(
        genre = listOf("Action", "Adventure", "Comedy")
    )
}
