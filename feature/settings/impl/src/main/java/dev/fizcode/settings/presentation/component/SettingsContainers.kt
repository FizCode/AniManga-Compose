package dev.fizcode.settings.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme

/** A titled group of settings drawn on one rounded card. */
@Composable
internal fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = title
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            content = content
        )
    }
}

/**
 * One row of a [SettingsSection]: a [leading] visual, a title with a subtitle and an optional
 * [trailing] visual.
 */
@Composable
internal fun SettingsItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    leading: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leading()
        Column(modifier = Modifier.weight(1f)) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                text = title
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                text = subtitle
            )
        }
        trailing?.invoke()
    }
}

/** Icon sized for the leading slot of a [SettingsItem]. */
@Composable
internal fun SettingsIcon(
    imageVector: ImageVector,
    contentDescription: String?
) = Icon(
    modifier = Modifier
        .padding(10.dp)
        .size(24.dp),
    imageVector = imageVector,
    tint = MaterialTheme.colorScheme.onBackground,
    contentDescription = contentDescription
)

@Preview(showBackground = true)
@Composable
private fun SettingsSectionPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        SettingsSection(title = "Theme") {
            SettingsItem(
                title = "Theme",
                subtitle = "Follow the System",
                onClick = {},
                leading = { SettingsIcon(CustomIcon.FILL_PALETTE, null) }
            )
        }
    }
}
