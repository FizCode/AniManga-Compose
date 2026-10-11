package dev.fizcode.bookmark.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.bookmark.util.Constant
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.card.EmptyCardComponent
import dev.fizcode.designsystem.component.card.MovieCardSmall
import dev.fizcode.designsystem.component.shimmer.MovieCardSmallShimmer
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * The bookmark list for each [UiState]; [emptyMessage] is shown when there is nothing to list.
 */
@Composable
internal fun BookmarkItemComponent(
    cardItem: UiState<List<BookmarkUiModel>>,
    emptyMessage: String,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    modifier: Modifier = Modifier
) = when (cardItem) {
    is UiState.Loading -> Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(Constant.SHIMMER_COUNT) { MovieCardSmallShimmer() }
    }

    is UiState.Success -> LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = cardItem.data, key = { it.mediaId }) { item ->
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

    is UiState.Empty -> EmptyCardComponent(
        modifier = modifier.fillMaxSize(),
        desc = emptyMessage
    )

    else -> {}
}

@Preview(showBackground = true)
@Composable
private fun BookmarkItemComponentPreview() {
    val bookmark = BookmarkUiModel(
        mediaId = 0,
        mediaType = "TV",
        posterPath = "",
        rating = "5.00",
        title = "BLEACH: Sennen Kessen-hen",
        subTitle = "TV | Eps. 10 of 12 in 6d 2h",
        studio = "Toei Animation",
        genre = persistentListOf("Action", "Adventure", "Comedy")
    )
    AniMangaAnimeAndMangaInfoTheme {
        BookmarkItemComponent(
            cardItem = UiState.Success(List(4) { bookmark.copy(mediaId = it) }),
            emptyMessage = "",
            onCardClick = { _, _ -> }
        )
    }
}
