package dev.fizcode.manga.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import dev.fizcode.manga.presentation.model.MangaType
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme

/**
 * Horizontally scrolling tabs, one per [MangaType]. The [selected] tab gets a highlighted pill.
 */
@Composable
internal fun MangaTypeTabs(
    selected: MangaType,
    onSelect: (MangaType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MangaType.entries.forEach { type ->
            val isSelected = type == selected
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
                        onClick = { onSelect(type) }
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.primary,
                    text = type.label
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaTypeTabsPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaTypeTabs(selected = MangaType.ALL, onSelect = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaTypeTabsOtherSelectedPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaTypeTabs(selected = MangaType.OTHER, onSelect = {})
    }
}
