package dev.fizcode.settings.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.fizcode.settings.util.Constant
import dev.fizcode.settings.presentation.model.label
import dev.fizcode.settings.presentation.model.icon
import dev.fizcode.settings.presentation.model.description
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.common.settings.NsfwFilter
import androidx.compose.ui.tooling.preview.Preview

/** Centered title with an icon and the description of the current choice, above its [content]. */
@Composable
internal fun FilterBlock(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsIcon(imageVector = icon, contentDescription = null)
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                text = title
            )
        }
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            text = description
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterBlockTitlingPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        FilterBlock(
            icon = CustomIcon.FILL_TEXT_FIELDS,
            title = Constant.TITLING_TITLE,
            description = TitleStyle.DEFAULT.description
        ) {
            FilterSegments(
                options = TitleStyle.entries,
                selected = TitleStyle.DEFAULT,
                label = { it.label },
                icon = { it.icon },
                onSelect = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterBlockNsfwPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        FilterBlock(
            icon = CustomIcon.FILL_NO_ADULT_CONTENT,
            title = Constant.NSFW_TITLE,
            description = NsfwFilter.GREY.description
        ) {
            FilterSegments(
                options = NsfwFilter.entries,
                selected = NsfwFilter.GREY,
                label = { it.label },
                onSelect = {}
            )
        }
    }
}
