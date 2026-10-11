package dev.fizcode.anime.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.anime.presentation.model.dummySeasonalUiModel
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.card.MovieCardLarge
import dev.fizcode.designsystem.component.shimmer.MovieCardLargeShimmer
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme

@Composable
internal fun CurrentSeason(
    headerTitle: String,
    cardItem: UiState<List<SeasonalUiModel>>,
    onHeaderClick: () -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) = when (cardItem) {
    is UiState.Loading -> {
        AnimeContent(
            headerTitle = headerTitle,
            onHeaderClick = {}
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(5) {
                    MovieCardLargeShimmer()
                }
            }
        }
    }

    is UiState.Success -> {
        AnimeContent(
            headerTitle = headerTitle,
            onHeaderClick = { onHeaderClick() }
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(items = cardItem.data, key = { it.id }) { item ->
                    MovieCardLarge(
                        posterPath = item.posterPath,
                        rating = item.rating,
                        title = item.title,
                        subTitle = item.releaseInfo,
                        studio = item.studio,
                        synopsis = item.synopsis,
                        genre = item.genre,
                        onCardClick = { onCardClick(item.mediaType, item.id) }
                    )
                }
            }
        }
    }

    else -> {}
}

@Composable
private fun CurrentSeasonPreviewContent(cardItem: UiState<List<SeasonalUiModel>>) {
    AniMangaAnimeAndMangaInfoTheme {
        CurrentSeason(
            headerTitle = "Current Season ☀️",
            cardItem = cardItem,
            onHeaderClick = {},
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun CurrentSeasonPreview() = CurrentSeasonPreviewContent(
    UiState.Success(List(5) { dummySeasonalUiModel })
)

@Preview(showBackground = true)
@Composable
private fun CurrentSeasonLoadingPreview() = CurrentSeasonPreviewContent(UiState.Loading)
