package dev.fizcode.anime.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.anime.presentation.model.dummySeasonalUiModel
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.designsystem.component.card.MovieCardLarge
import dev.fizcode.designsystem.component.shimmer.MovieCardLargeShimmer

@Composable
internal fun CurrentSeason(
    headerTitle: String,
    cardItem: UiState<List<SeasonalUiModel>>,
    onHeaderClick: () -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    AnimeContent(
        headerTitle = headerTitle,
        onHeaderClick = { onHeaderClick() }
    )
    {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            when (cardItem) {
                is UiState.Success -> {
                    itemsIndexed(items = cardItem.data) { _, item ->
                        MovieCardLarge(
                            posterPath = item.posterPath,
                            rating = item.rating,
                            title = item.title,
                            subTitle = item.releaseInfo,
                            studio = item.studio.first(), // TODO: change to list
                            synopsis = item.synopsis,
                            genre = item.genre,
                            onCardClick = { onCardClick(item.mediaType, item.id) }
                        )
                    }
                }

                else -> {
                    items(count = 5) {
                        MovieCardLargeShimmer()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun CurrentSeasonPreview() {
    CurrentSeason(
        headerTitle = "Current Season ☀️",
        cardItem = UiState.Success(
            listOf(
                dummySeasonalUiModel,
                dummySeasonalUiModel,
                dummySeasonalUiModel,
                dummySeasonalUiModel,
                dummySeasonalUiModel
            )
        ),
        onHeaderClick = {},
        onCardClick = { _, _ -> }
    )
}
