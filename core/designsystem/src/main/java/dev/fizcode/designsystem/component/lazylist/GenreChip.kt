package dev.fizcode.designsystem.component.lazylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.chip.GenreChip

/**
 * Horizontally scrolling row (a plain scrollable [Row], genre lists are short so no lazy list is needed) of [GenreChip]s spaced 4dp apart.
 *
 * @param genre genre names, one chip per entry.
 * @param modifier modifier applied to the row.
 */
@Composable
fun GenreChip(
    genre: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        genre.forEach { GenreChip(genre = it) }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChipPreview() {
    GenreChip(
        genre = listOf("Action", "Adventure", "Comedy")
    )
}
