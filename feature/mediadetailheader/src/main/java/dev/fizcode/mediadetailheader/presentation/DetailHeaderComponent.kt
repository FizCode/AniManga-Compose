package dev.fizcode.mediadetailheader.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.fizcode.common.util.toCommaSeparatorMaxM
import dev.fizcode.designsystem.component.chip.GenreChip
import dev.fizcode.designsystem.component.other.FiveStarReview
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.util.base.shimmerBrush
import dev.fizcode.mediadetailheader.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetailheader.util.Constant

@Composable
fun DetailHeaderComponent(
    header: AnimeDetailsHeaderUiModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SliderLargeImage(header.pictures)
        Spacer(modifier = Modifier.height(8.dp))
        DetailAnimeTitle(
            posterPath = header.posterPath,
            title = header.title,
            mediaType = header.mediaType,
            releaseSeason = header.releaseSeason,
            studios = header.studio
        )
        DetailEpisodesInfoCard(
            releaseInfo = header.releaseInfo,
            duration = header.duration
        )
        LittleInfoCard(
            rank = header.rank,
            popularity = header.popularity,
            members = header.members,
            favorites = header.favorites
        )
        ScoresAndBookmark(
            score = header.score,
            totalVote = header.totalVote
        )
        GenreChips(genre = header.genre)
    }
}

// TODO: Change to swappable image with dots indicator
@Composable
private fun SliderLargeImage(pictures: List<String>) = AsyncImage(
    modifier = Modifier
        .background(shimmerBrush(targetValue = 1300f))
        .fillMaxWidth()
        .height(202.dp),
    contentScale = ContentScale.Crop,
    contentDescription = Constant.SLIDER_IMAGES,
    model = pictures.first()
)

@Composable
private fun DetailAnimeTitle(
    posterPath: String,
    title: String,
    mediaType: String,
    releaseSeason: String,
    studios: String
) = Row(
    modifier = Modifier.padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(shimmerBrush(targetValue = 1300f))
            .width(124.dp)
            .height(178.dp),
        contentScale = ContentScale.Crop,
        contentDescription = Constant.POSTER_IMAGE,
        model = posterPath
    )
    Column {
        Text(
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = title
        )
        Spacer(Modifier.height(4.dp))
        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            text = "$mediaType | $releaseSeason"
        )
        Text(
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            text = studios
        )
    }
}

@Composable
private fun DetailEpisodesInfoCard(
    releaseInfo: String,
    duration: String,
) = Column(
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 8.dp)
        .background(MaterialTheme.colorScheme.surfaceContainerLow),
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        text = releaseInfo
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        text = duration
    )
}

@Composable
private fun LittleInfoCard(
    rank: Int,
    popularity: Int,
    members: Int,
    favorites: Int
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
            value = "#${rank.toCommaSeparatorMaxM()}"
        )
    }
    item {
        HighlightedBadge(
            icon = CustomIcon.OUTL_SHOW_CHART,
            title = Constant.POPULARITY,
            value = "#${popularity.toCommaSeparatorMaxM()}"
        )
    }
    item {
        HighlightedBadge(
            icon = CustomIcon.OUTL_PEOPLE,
            title = Constant.MEMBERS,
            value = members.toCommaSeparatorMaxM()
        )
    }
    item {
        HighlightedBadge(
            icon = CustomIcon.OUTL_FAVORITE_BORDER,
            title = Constant.FAVORITES,
            value = favorites.toCommaSeparatorMaxM()
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

@Composable
private fun ScoresAndBookmark(
    score: Double,
    totalVote: Int
) = Row(
    modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
) {
    Row(
        modifier = Modifier.weight(1F),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1F),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium,
            text = score.toString()
        )
        Column(modifier = Modifier.weight(1F)) {
            FiveStarReview(score = score)
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                text = "${totalVote.toCommaSeparatorMaxM()} ${Constant.VOTES}"
            )
        }
    }
    Spacer(Modifier.width(8.dp))
    Button(
        modifier = Modifier
            .weight(1F)
            .padding(vertical = 8.dp),
        onClick = {}
    ) {
        Icon(
            imageVector = CustomIcon.OUTL_BOOKMARK_ADD,
            contentDescription = Constant.BOOKMARK_ICON
        )
        Spacer(Modifier.width(8.dp))
        Text(Constant.BOOKMARK)
    }
}

@Composable
private fun GenreChips(genre: List<String>) = Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    LazyRow(
        modifier = Modifier.wrapContentWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = genre) { _, item ->
            GenreChip(item)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailHeaderComponentPreview() {
    DetailHeaderComponent(
        AnimeDetailsHeaderUiModel(
            title = "Lorem"
        )
    )
}