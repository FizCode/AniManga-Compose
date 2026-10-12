package dev.fizcode.bookmark.presentation.component

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
import dev.fizcode.bookmark.presentation.model.BookmarkTab
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.designsystem.util.base.shimmerBrush

/** Horizontally scrolling tabs, one per [BookmarkTab]. The [selected] tab gets a highlighted pill. */
@Composable
internal fun BookmarkTabs(
    selected: BookmarkTab,
    onSelect: (BookmarkTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BookmarkTab.entries.forEach { tab ->
            val isSelected = tab == selected
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
                        onClick = { onSelect(tab) }
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.primary,
                    text = tab.label
                )
            }
        }
    }
}

/** Loading skeleton matching the layout of [BookmarkTabs]. */
@Composable
internal fun BookmarkTabsShimmer(modifier: Modifier = Modifier) {
    val shimmer = shimmerBrush()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState(), enabled = false),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BookmarkTab.entries.forEach { tab ->
            Box(
                modifier = Modifier
                    .width((tab.label.length * 9 + 32).dp)
                    .height(44.dp)
                    .clip(CircleShape)
                    .background(shimmer)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkTabsPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        BookmarkTabs(selected = BookmarkTab.ANIME, onSelect = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkTabsShimmerPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        BookmarkTabsShimmer()
    }
}
