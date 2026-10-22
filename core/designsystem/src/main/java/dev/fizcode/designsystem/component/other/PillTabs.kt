package dev.fizcode.designsystem.component.other

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.designsystem.util.base.shimmer

/**
 * Horizontally scrolling tabs, one per entry of [items]. The [selected] tab gets a highlighted pill.
 */
@Composable
fun <T> PillTabs(
    items: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEach { item ->
            val isSelected = item == selected
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.background
                    )
                    .selectable(
                        selected = isSelected,
                        role = Role.Tab,
                        onClick = { onSelect(item) }
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.primary,
                    text = label(item)
                )
            }
        }
    }
}

/** Loading skeleton matching the layout of [PillTabs], one placeholder per entry of [labels]. */
@Composable
fun PillTabsShimmer(labels: List<String>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState(), enabled = false),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        labels.forEach { label ->
            Box(
                modifier = Modifier
                    .width((label.length * 9 + 32).dp)
                    .height(44.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PillTabsPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        val items = listOf("All", "Manga", "Novel")
        PillTabs(items = items, selected = items.first(), label = { it }, onSelect = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PillTabsShimmerPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        PillTabsShimmer(labels = listOf("All", "Manga", "Novel"))
    }
}
