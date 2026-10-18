package dev.fizcode.manga.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.other.SearchTextFieldComponent
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.manga.presentation.model.MangaSort
import dev.fizcode.manga.util.Constant
import dev.fizcode.designsystem.util.DashboardTopBarHeight
import androidx.compose.foundation.layout.height
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme

/**
 * Search field on the left and, on the right, the selected [sort] with a filter icon.
 * Tapping the latter opens a menu to pick another [MangaSort].
 */
@Composable
internal fun MangaHeader(
    queryState: TextFieldState,
    sort: MangaSort,
    onSortSelect: (MangaSort) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(DashboardTopBarHeight),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchTextFieldComponent(
            modifier = Modifier.weight(1f),
            state = queryState,
            placeholder = Constant.PLACEHOLDER
        )
        Box {
            Row(
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = CustomIcon.ROUND_FILTER_LIST,
                    contentDescription = Constant.FILTER_ICON
                )
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    text = sort.label
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                MangaSort.entries.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.label) },
                        onClick = {
                            expanded = false
                            onSortSelect(item)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaHeaderPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaHeader(
            queryState = rememberTextFieldState(),
            sort = MangaSort.RANKING,
            onSortSelect = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaHeaderWithQueryPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaHeader(
            queryState = rememberTextFieldState(initialText = "Berserk"),
            sort = MangaSort.RANKING,
            onSortSelect = {}
        )
    }
}
