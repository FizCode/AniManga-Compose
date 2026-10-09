package dev.fizcode.search.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.component.chip.GenreChip
import dev.fizcode.designsystem.component.chip.RatingChip
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.search.presentation.model.SearchResultUiModel
import dev.fizcode.search.presentation.model.dummySearchResultUiModel
import dev.fizcode.search.presentation.util.highlightQuery
import dev.fizcode.search.util.Constant

@Composable
internal fun SearchResultItemComponent(
    item: SearchResultUiModel,
    query: String,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val highlightColor = MaterialTheme.colorScheme.primary
    val title = remember(item.title, query, highlightColor) {
        highlightQuery(text = item.title, query = query, color = highlightColor)
    }

    Card(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(POSTER_WIDTH.dp)
                    .height(POSTER_HEIGHT.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(shimmerBrush()),
                contentScale = ContentScale.Crop,
                contentDescription = Constant.POSTER_IMAGE,
                model = item.posterPath
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = title
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingChip(rating = item.rating)
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline,
                        text = item.subTitle
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    maxLines = 2
                ) {
                    item.genre.forEach { GenreChip(genre = it) }
                }
            }
        }
    }
}

private const val POSTER_WIDTH = 108
private const val POSTER_HEIGHT = 156

@Preview(showBackground = true)
@Composable
private fun SearchResultItemComponentPreview() {
    SearchResultItemComponent(
        item = dummySearchResultUiModel,
        query = "Ble",
        onCardClick = {}
    )
}
