package dev.fizcode.settings.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.settings.presentation.model.icon
import dev.fizcode.settings.presentation.model.label

private val OuterCorner = 24.dp
private val InnerCorner = 4.dp

/**
 * A single choice row of joined segments. The [selected] segment is highlighted and, when [icon]
 * is given, it is the only one that shows its label, the others show just their icon.
 */
@Composable
internal fun <T> FilterSegments(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    icon: ((T) -> ImageVector)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = option == selected
            val weight by animateFloatAsState(
                targetValue = if (icon != null && !isSelected) 1f else 2.4f,
                label = "segmentWeight"
            )
            val shape = RoundedCornerShape(
                topStart = if (index == 0) OuterCorner else InnerCorner,
                bottomStart = if (index == 0) OuterCorner else InnerCorner,
                topEnd = if (index == options.lastIndex) OuterCorner else InnerCorner,
                bottomEnd = if (index == options.lastIndex) OuterCorner else InnerCorner
            )
            val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSecondaryContainer

            Row(
                modifier = Modifier
                    .weight(weight)
                    .clip(shape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondaryContainer
                    )
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = { onSelect(option) }
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = icon(option),
                        tint = contentColor,
                        // The label is read out even when only the icon is visible.
                        contentDescription = label(option)
                    )
                }
                if (icon == null || isSelected) {
                    Text(
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor,
                        maxLines = 1,
                        text = label(option)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TitleStyleSegmentsPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        FilterSegments(
            modifier = Modifier.padding(16.dp),
            options = TitleStyle.entries,
            selected = TitleStyle.DEFAULT,
            label = { it.label },
            icon = { it.icon },
            onSelect = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NsfwSegmentsPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        FilterSegments(
            modifier = Modifier.padding(16.dp),
            options = NsfwFilter.entries,
            selected = NsfwFilter.SAFE,
            label = { it.label },
            onSelect = {}
        )
    }
}
