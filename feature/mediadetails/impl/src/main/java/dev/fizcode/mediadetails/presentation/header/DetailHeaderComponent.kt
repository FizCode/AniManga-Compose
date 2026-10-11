package dev.fizcode.mediadetails.presentation.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.mediadetails.presentation.header.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.header.anime.DetailMediaTitle
import dev.fizcode.mediadetails.presentation.header.anime.DetailEpisodesInfoCard
import dev.fizcode.mediadetails.presentation.header.anime.GenreChips
import dev.fizcode.mediadetails.presentation.header.anime.LittleInfoCard
import dev.fizcode.mediadetails.presentation.header.anime.ScoresAndBookmark
import dev.fizcode.mediadetails.presentation.header.anime.SliderLargeImage
import kotlinx.collections.immutable.persistentListOf
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData

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
        DetailMediaTitle(
            posterPath = header.posterPath,
            title = header.title,
            subtitle = listOf(header.mediaType, header.releaseSeason)
                .filter { it.isNotBlank() }
                .joinToString(" | "),
            caption = header.studio
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
    AniMangaAnimeAndMangaInfoTheme {
        DetailHeaderComponent(
            isBookmarked = true,
            header = MediaDetailsPreviewData.anime.animeDetailsHeaderUiModel,
            onClickBookmark = {},
            onPictureClick = { _ -> }
        )
    }
}
