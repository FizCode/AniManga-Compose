package dev.fizcode.designsystem.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.fizcode.designsystem.component.chip.RatingChip
import dev.fizcode.designsystem.component.lazylist.GenreChip
import dev.fizcode.designsystem.icon.CustomIcon.ROUND_STAR_RATE
import dev.fizcode.designsystem.theme.YellowGold
import dev.fizcode.designsystem.util.Constant.Component
import dev.fizcode.designsystem.util.base.shimmerBackground
import dev.fizcode.designsystem.util.highlightQuery

/**
 * Poster image with a shimmer placeholder that only animates until the image has loaded.
 * The shimmer is read in the draw phase, so it does not recompose the card.
 */
@Composable
private fun PosterImage(
    posterPath: String,
    modifier: Modifier = Modifier
) {
    var loaded by remember(posterPath) { mutableStateOf(false) }
    val context = LocalContext.current
    val request = remember(posterPath) {
        ImageRequest.Builder(context)
            .data(posterPath)
            .crossfade(true)
            .build()
    }
    AsyncImage(
        modifier = modifier
            .clip(PosterShape)
            .then(if (loaded) Modifier else Modifier.shimmerBackground()),
        contentScale = ContentScale.Crop,
        contentDescription = Component.CARD_IMAGE,
        model = request,
        onSuccess = { loaded = true }
    )
}

private val PosterShape = RoundedCornerShape(8.dp)

/**
 * Shared card shell for every movie card: transparent [Card] clipped to [shape].
 */
@Composable
private fun MediaCardContainer(
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onClick,
        content = content
    )
}

/**
 * Poster with the [RatingChip] overlaid on its corner.
 */
@Composable
private fun PosterWithRating(
    posterPath: String,
    rating: String,
    modifier: Modifier = Modifier
) {
    Box {
        PosterImage(posterPath = posterPath, modifier = modifier)
        RatingChip(rating = rating)
    }
}

/**
 * Single-style text used by the card bodies: [maxLines] with ellipsis, full width.
 */
@Composable
private fun CardText(
    text: String,
    style: TextStyle,
    maxLines: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) = CardText(
    text = AnnotatedString(text),
    style = style,
    maxLines = maxLines,
    modifier = modifier,
    color = color,
    textAlign = textAlign
)

@Composable
private fun CardText(
    text: AnnotatedString,
    style: TextStyle,
    maxLines: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        style = style,
        color = color,
        text = text
    )
}

/**
 * Compact vertical poster card (140dp wide): poster with rating chip overlay and a two-line centered title.
 *
 * @param modifier modifier applied to the card.
 * @param posterPath poster image url, a shimmer is shown while it loads.
 * @param title media title, truncated after two lines.
 * @param rating rating text shown in the [RatingChip] over the poster.
 * @param onCardClick invoked when the card is clicked.
 */
@Composable
fun MovieCardSimple(
    modifier: Modifier = Modifier,
    posterPath: String,
    title: String,
    rating: String,
    onCardClick: () -> Unit
) {
    MediaCardContainer(
        modifier = modifier.width(140.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            PosterWithRating(
                posterPath = posterPath,
                rating = rating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(178.dp)
            )
            CardText(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Horizontal list-item card: 100dp square poster with rating chip, beside title, subtitle,
 * studio and a row of genre chips.
 *
 * @param modifier modifier applied to the card.
 * @param posterPath poster image url, a shimmer is shown while it loads.
 * @param rating rating text shown in the [RatingChip] over the poster.
 * @param title media title, single line.
 * @param subTitle secondary info such as type, episode count and status.
 * @param studio studio name.
 * @param genre genre names rendered through [GenreChip].
 * @param onCardClick invoked when the card is clicked.
 */
@Composable
fun MovieCardSmall(
    modifier: Modifier = Modifier,
    posterPath: String,
    rating: String,
    title: String,
    subTitle: String,
    studio: String,
    genre: List<String>,
    onCardClick: () -> Unit
) {
    MediaCardContainer(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PosterWithRating(
                posterPath = posterPath,
                rating = rating,
                modifier = Modifier.size(100.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                CardText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                CardText(
                    text = subTitle,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.outline
                )
                CardText(
                    text = studio,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.outline
                )
                GenreChip(genre = genre, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

/**
 * Compact horizontal card: small poster beside title, rating, subtitle and genre chips.
 *
 * @param modifier modifier applied to the card.
 * @param posterPath poster image url, a shimmer is shown while it loads.
 * @param title media title, truncated after two lines.
 * @param query search text; every case-insensitive match inside [title] is highlighted. Blank disables it.
 * @param rating rating text shown next to a gold star.
 * @param subTitle secondary info such as type, episode count and status.
 * @param genre genre names rendered through [GenreChip].
 * @param onCardClick invoked when the card is clicked.
 */
@Composable
fun MovieCardMedium(
    modifier: Modifier = Modifier,
    posterPath: String,
    title: String,
    rating: String,
    subTitle: String,
    genre: List<String>,
    onCardClick: () -> Unit,
    query: String = ""
) {
    val highlightColor = MaterialTheme.colorScheme.primary
    val highlightedTitle = remember(title, query, highlightColor) {
        highlightQuery(text = title, query = query, color = highlightColor)
    }
    MediaCardContainer(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PosterImage(
                posterPath = posterPath,
                modifier = Modifier
                    .width(54.dp)
                    .height(78.dp)
            )
            Column {
                CardText(
                    text = highlightedTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        Modifier.padding(start = 12.dp, top = 6.dp, end = 8.dp, bottom = 6.dp),
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelMedium,
                            text = rating
                        )
                        Icon(
                            modifier = Modifier.size(16.dp),
                            tint = YellowGold,
                            imageVector = ROUND_STAR_RATE,
                            contentDescription = Component.CHIP_ICON
                        )
                    }
                    CardText(
                        text = subTitle,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                GenreChip(genre = genre, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

/**
 * Large horizontal card (328dp wide): tall poster with rating chip, beside title, subtitle,
 * studio, a two-line synopsis and a row of genre chips.
 *
 * @param modifier modifier applied to the card.
 * @param posterPath poster image url, a shimmer is shown while it loads.
 * @param rating rating text shown in the [RatingChip] over the poster.
 * @param title media title, truncated after two lines.
 * @param subTitle secondary info such as type, episode count and status.
 * @param studio studio name.
 * @param synopsis short description, truncated after two lines.
 * @param genre genre names rendered through [GenreChip].
 * @param onCardClick invoked when the card is clicked.
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
    MediaCardContainer(
        modifier = modifier.width(328.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PosterWithRating(
                posterPath = posterPath,
                rating = rating,
                modifier = Modifier
                    .width(114.dp)
                    .height(164.dp)
            )
            Column {
                CardText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                CardText(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.outline
                )
                CardText(
                    text = studio,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                CardText(
                    text = synopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                GenreChip(genre = genre, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardSimplePreview() {
    MovieCardSimple(
        posterPath = "",
        title = "BLEACH: Sennen Kessen-hen",
        rating = "5.00",
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MovieCardSmallPreview() {
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

@Preview(showBackground = true)
@Composable
private fun MovieCardMediumPreview() {
    MovieCardMedium(
        posterPath = "",
        title = "BLEACH: Sennen Kessen-hen",
        rating = "5.00",
        subTitle = "TV | Episodes 12 | Finished",
        genre = listOf("Action", "Adventure", "Comedy"),
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MovieCardLargePreview() {
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
