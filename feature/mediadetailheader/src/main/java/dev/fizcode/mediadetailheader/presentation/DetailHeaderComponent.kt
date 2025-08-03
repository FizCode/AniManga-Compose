package dev.fizcode.mediadetailheader.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.mediadetailheader.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetailheader.presentation.anime.DetailAnimeTitle
import dev.fizcode.mediadetailheader.presentation.anime.DetailEpisodesInfoCard
import dev.fizcode.mediadetailheader.presentation.anime.GenreChips
import dev.fizcode.mediadetailheader.presentation.anime.LittleInfoCard
import dev.fizcode.mediadetailheader.presentation.anime.ScoresAndBookmark
import dev.fizcode.mediadetailheader.presentation.anime.SliderLargeImage
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DetailHeaderComponent(
    modifier: Modifier = Modifier,
    isBookmarked: Boolean,
    header: AnimeDetailsHeaderUiModel,
    onPictureClick: (String) -> Unit,
    onClickBookmark: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SliderLargeImage(
            pictures = header.pictures,
            largePictures = header.largePicture,
            onPictureClick = onPictureClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        DetailAnimeTitle(
            posterPath = header.posterPath,
            title = header.title,
            mediaType = header.mediaType,
            releaseSeason = header.releaseSeason,
            studios = header.studio
        )
        DetailEpisodesInfoCard(
            modifier = modifier,
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
            isBookmarked = isBookmarked,
            score = header.score,
            stars = header.stars,
            totalVote = header.totalVote,
            onClickBookmark = onClickBookmark
        )
        GenreChips(genre = header.genre)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailHeaderComponentPreview() {
    val dummyHeaderData = AnimeDetailsHeaderUiModel(
        pictures = persistentListOf(),
        largePicture = persistentListOf(),
        posterPath = "",
        title = "Lorem Ipsum",
        mediaType = "Media Type",
        releaseSeason = "Spring 2025",
        studio = "Studio Name",
        releaseInfo = "Ep. 1 of 12, currently airing",
        duration = "24 mins. per ep.",
        rank = "#10",
        popularity = "#10",
        members = "1,234",
        favorites = "1,234",
        score = "5.00",
        stars = 5.0,
        totalVote = "1,000",
        genre = persistentListOf()
    )
    DetailHeaderComponent(
        isBookmarked = true,
        header = dummyHeaderData,
        onClickBookmark = {},
        onPictureClick = { _ -> }
    )
}