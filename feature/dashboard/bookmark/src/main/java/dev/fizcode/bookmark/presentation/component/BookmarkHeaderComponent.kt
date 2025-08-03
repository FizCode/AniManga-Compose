package dev.fizcode.bookmark.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.bookmark.util.Constant
import dev.fizcode.designsystem.component.other.SearchSimpleRowComponent
import dev.fizcode.designsystem.icon.CustomIcon

@Composable
internal fun BookmarkHeaderComponent(
    searchValue: String,
    dropdownLabel: String,
    onClickSearch: () -> Unit,
    onClickDropdown: () -> Unit
) = Row(
    modifier = Modifier.padding(end = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    SearchSimpleRowComponent(
        modifier = Modifier.weight(1F),
        value = searchValue,
        onClickSearch = onClickSearch
    )
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickDropdown }
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            text = dropdownLabel
        )
        Icon(
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary,
            imageVector = CustomIcon.ROUND_ARROW_DROP_UP,
            contentDescription = Constant.DROPDOWN_ICON
        )
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkHeaderPreview() {
    BookmarkHeaderComponent(
        searchValue = "Search",
        dropdownLabel = "Manga",
        onClickSearch = {},
        onClickDropdown = {}
    )
}
