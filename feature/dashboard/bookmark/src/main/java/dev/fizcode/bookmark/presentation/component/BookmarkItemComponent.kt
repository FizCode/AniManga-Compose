package dev.fizcode.bookmark.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.card.EmptyCardComponent
import dev.fizcode.designsystem.component.card.MovieCardSmall
import dev.fizcode.designsystem.component.shimmer.MovieCardSmallShimmer
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun BookmarkItemComponent(
    cardItem: UiState<List<BookmarkUiModel>>,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) = when (cardItem) {
    is UiState.Loading -> {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) {
                MovieCardSmallShimmer()
            }
        }
    }

    is UiState.Success -> {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cardItem.data.forEachIndexed { index, item ->
                key(item.mediaId) {
                    MovieCardSmall(
                        posterPath = item.posterPath,
                        rating = item.rating,
                        title = item.title,
                        subTitle = item.subTitle,
                        studio = item.studio,
                        genre = item.genre,
                        onCardClick = { onCardClick(item.mediaType, item.mediaId) }
                    )
                }
            }
        }
    }

    is UiState.Empty -> {
        EmptyCardComponent(
            modifier = Modifier.fillMaxSize(),
            desc = "Empty Bookmark."
        )
    }

    else -> {}
}

@Preview(showBackground = true)
@Composable
private fun TopRankingPreview() {
    val dummyTopRankingUiModel = BookmarkUiModel(
        mediaId = 0,
        mediaType = "tv",
        posterPath = "",
        rating = "5.00",
        title = "BLEACH: Sennen Kessen-hen",
        subTitle = "TV | Eps. 10 of 12 in 6d 2h",
        studio = "Toei Animation",
        genre = persistentListOf("Action", "Adventure", "Comedy")
    )
    BookmarkItemComponent(
        cardItem = UiState.Success(
            listOf(
                dummyTopRankingUiModel,
                dummyTopRankingUiModel,
                dummyTopRankingUiModel,
                dummyTopRankingUiModel,
            )
        ),
        onCardClick = { _, _ -> }
    )
}
