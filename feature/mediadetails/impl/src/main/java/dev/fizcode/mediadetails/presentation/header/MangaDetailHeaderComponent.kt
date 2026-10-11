package dev.fizcode.mediadetails.presentation.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.mediadetails.presentation.header.anime.DetailEpisodesInfoCard
import dev.fizcode.mediadetails.presentation.header.anime.DetailMediaTitle
import dev.fizcode.mediadetails.presentation.header.anime.GenreChips
import dev.fizcode.mediadetails.presentation.header.anime.LittleInfoCard
import dev.fizcode.mediadetails.presentation.header.anime.ScoresAndBookmark
import dev.fizcode.mediadetails.presentation.header.anime.SliderLargeImage
import dev.fizcode.mediadetails.presentation.header.model.MangaDetailsHeaderUiModel
import kotlinx.collections.immutable.persistentListOf
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData

@Composable
fun MangaDetailHeaderComponent(
    modifier: Modifier = Modifier,
    isBookmarked: Boolean,
    header: MangaDetailsHeaderUiModel,
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
        DetailMediaTitle(
            posterPath = header.posterPath,
            title = header.title,
            subtitle = header.serialization,
            caption = header.mediaType
        )
        DetailEpisodesInfoCard(
            modifier = modifier,
            releaseInfo = header.publishInfo,
            duration = header.publishedDates
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
private fun MangaDetailHeaderComponentPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaDetailHeaderComponent(
            isBookmarked = false,
            header = MediaDetailsPreviewData.manga.mangaDetailsHeaderUiModel,
            onPictureClick = {},
            onClickBookmark = {}
        )
    }
}
