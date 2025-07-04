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
import dev.fizcode.anime.presentation.model.TopAiringUiModel
import dev.fizcode.anime.presentation.model.dummyTopAiringUiModel
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.designsystem.component.card.MovieCardSimple
import dev.fizcode.designsystem.component.shimmer.MovieCardSimpleShimmer

@Composable
internal fun TopAiring(
    headerTitle: String,
    cardItem: UiState<List<TopAiringUiModel>>,
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
                    MovieCardSimpleShimmer()
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
                    MovieCardSimple(
                        posterPath = item.posterPath,
                        rating = item.rating,
                        title = item.title,
                        onCardClick = { onCardClick(item.mediaType, item.id) }
                    )
                }
            }
        }
    }

    else -> {}
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun TopAiringPreview() {
    TopAiring(
        headerTitle = "Top Airing ✨",
        cardItem = UiState.Success(
            listOf(
                dummyTopAiringUiModel,
                dummyTopAiringUiModel,
                dummyTopAiringUiModel,
                dummyTopAiringUiModel,
                dummyTopAiringUiModel
            )
        ),
        onHeaderClick = {},
        onCardClick = { _, _ -> }
    )
}
