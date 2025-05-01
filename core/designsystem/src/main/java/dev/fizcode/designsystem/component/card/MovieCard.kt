package dev.fizcode.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.designsystem.component.chip.RatingChip
import dev.fizcode.designsystem.component.lazylist.LazyRowGenreChip
import dev.fizcode.designsystem.util.DesignSystemConstant.Component
import dev.fizcode.designsystem.util.base.shimmerBrush

/**
 * TODO: Add description
 */
@Composable
fun MovieCardSimple(
    posterPath: String,
    title: String,
    rating: String,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .width(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onCardClick
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f))
                        .fillMaxWidth()
                        .height(178.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = Component.CARD_IMAGE,
                    model = posterPath
                )
                RatingChip(rating = rating)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                text = title
            )
        }
    }
}

/**
 * TODO: Add description
 */
@Composable
fun MovieCardSmall(
    posterPath: String,
    rating: String,
    title: String,
    subTitle: String,
    studio: String,
    genre: List<String>,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier.clip(RoundedCornerShape(6.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f))
                        .size(100.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = Component.CARD_IMAGE,
                    model = posterPath
                )
                RatingChip(rating = rating)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = title
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    text = subTitle
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    text = studio
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRowGenreChip(genre = genre)
            }
        }

    }
}

/**
 * TODO: Add description
 */
@Composable
fun MovieCardLarge(
    modifier: Modifier = Modifier,
    posterPath: String,
    rating: String,
    title: String,
    subTitle: String,
    studio: String,
    synopsis: String,
    genre: List<String>,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .width(328.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush(targetValue = 1300f))
                        .width(114.dp)
                        .height(164.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = Component.CARD_IMAGE,
                    model = posterPath
                )
                RatingChip(rating = rating)
            }
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = title
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    text = subTitle
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    text = studio
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = synopsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRowGenreChip(genre = genre)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardPreview() {
    Column {
        MovieCardSimple(
            posterPath = "",
            title = "BLEACH: Sennen Kessen-hen",
            rating = "5.00",
            onCardClick = {}
        )
        MovieCardSmall(
            posterPath = "",
            title = "BLEACH: Sennen Kessen-hen",
            subTitle = "TV | Episodes 12 | Finished",
            studio = "Toei Animation",
            rating = "5.00",
            genre = listOf("Action", "Adventure", "Comedy"),
            onCardClick = {}
        )
        MovieCardLarge(
            posterPath = "",
            title = "BLEACH: Sennen Kessen-hen",
            subTitle = "TV | Episodes 12 | Finished",
            studio = "Toei Animation",
            rating = "5.00",
            genre = listOf("Action", "Adventure", "Comedy"),
            synopsis = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            onCardClick = {}
        )
    }
}
