package dev.fizcode.mediadetailheader.presentation.anime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetailheader.util.Constant

@Composable
internal fun LittleInfoCard(
    rank: String,
    popularity: String,
    members: String,
    favorites: String
) = LazyRow(
    modifier = Modifier
        .padding(vertical = 6.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    item {
        HighlightedBadge(
            icon = CustomIcon.OUTL_WORKSPACE_PREMIUM,
            title = Constant.RANK,
            value = rank
        )
    }
    item {
        HighlightedBadge(
            icon = CustomIcon.OUTL_SHOW_CHART,
            title = Constant.POPULARITY,
            value = popularity
        )
    }
    item {
        HighlightedBadge(
            icon = CustomIcon.OUTL_PEOPLE,
            title = Constant.MEMBERS,
            value = members
        )
    }
    item {
        HighlightedBadge(
            icon = CustomIcon.OUTL_FAVORITE_BORDER,
            title = Constant.FAVORITES,
            value = favorites
        )
    }
}

@Composable
private fun HighlightedBadge(
    icon: ImageVector,
    title: String,
    value: String
) = Row(
    modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surfaceContainerLow)
        .padding(horizontal = 8.dp)
        .widthIn(90.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Icon(
        imageVector = icon,
        tint = MaterialTheme.colorScheme.secondary,
        contentDescription = Constant.BADGE_ICON
    )
    Column(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        Text(
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelSmall,
            text = title
        )
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            text = value
        )
    }
}
