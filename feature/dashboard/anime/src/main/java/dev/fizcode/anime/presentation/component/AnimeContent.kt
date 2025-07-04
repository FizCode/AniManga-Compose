package dev.fizcode.anime.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.anime.util.Constant
import dev.fizcode.designsystem.component.card.MovieCardSmall
import dev.fizcode.designsystem.icon.CustomIcon

@Composable
internal fun AnimeContent(
    headerTitle: String,
    onHeaderClick: () -> Unit,
    content: @Composable () -> Unit
) = Column(
    modifier = Modifier.fillMaxWidth()
) {
    Row(
        modifier = Modifier
            .clickable { onHeaderClick() }
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineSmall,
            text = headerTitle
        )
        IconButton(onClick = { onHeaderClick() }) {
            Icon(
                tint = MaterialTheme.colorScheme.secondary,
                imageVector = CustomIcon.OUTL_ARROW_RIGHT,
                contentDescription = Constant.HEADER_ICON
            )
        }
    }

    content()
}

@Preview(showBackground = true)
@Composable
private fun AnimeContentPreview() {
    AnimeContent(
        headerTitle = "Current Season ☀️",
        onHeaderClick = {}
    ) {
        MovieCardSmall(
            posterPath = "",
            title = "BLEACH: Sennen Kessen-hen",
            subTitle = "TV | Episodes 12 | Finished",
            studio = "Toei Animation",
            rating = "5.00",
            genre = listOf("Action", "Adventure", "Comedy"),
            onCardClick = {}
        )
    }
}
