package dev.fizcode.ranking.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.card.EmptyCardComponent
import dev.fizcode.designsystem.component.card.ErrorCardWithButtonComponent
import dev.fizcode.designsystem.component.card.MovieCardLarge
import dev.fizcode.designsystem.component.shimmer.MovieCardLargeShimmer
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.ranking.presentation.model.RankingUiModel
import dev.fizcode.ranking.presentation.model.dummyRankingUiModel
import dev.fizcode.ranking.util.Constant

/**
 * The ranking list for each [UiState]. Every [query] match inside a card title is highlighted.
 */
@Composable
internal fun RankingListComponent(
    result: UiState<List<RankingUiModel>>,
    query: String,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) = when (result) {
    is UiState.Loading -> Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(Constant.SHIMMER_COUNT) { MovieCardLargeShimmer(Modifier.fillMaxWidth()) }
    }

    is UiState.Success -> LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = result.data, key = { it.id }) { item ->
            MovieCardLarge(
                modifier = Modifier.fillMaxWidth(),
                posterPath = item.posterPath,
                rating = item.rating,
                title = "${item.rank}. ${item.title}",
                query = query,
                subTitle = item.releaseInfo,
                studio = item.studio,
                synopsis = item.synopsis,
                genre = item.genre,
                onCardClick = { onCardClick(item.mediaType, item.id) }
            )
        }
    }

    is UiState.Empty -> EmptyCardComponent(
        modifier = modifier,
        desc = if (query.isNotBlank()) Constant.EMPTY_QUERY_RESULT else Constant.EMPTY_RESULT
    )

    is UiState.ErrorRedirectResponse -> RankingError(result.message, onRetry, modifier)
    is UiState.ErrorClientRequest -> RankingError(result.message, onRetry, modifier)
    is UiState.ErrorServerResponse -> RankingError(result.message, onRetry, modifier)
    is UiState.ErrorException -> RankingError(result.message, onRetry, modifier)
}

@Composable
private fun RankingError(
    message: String?,
    onRetry: () -> Unit,
    modifier: Modifier
) = ErrorCardWithButtonComponent(
    modifier = modifier,
    errorCode = Constant.ERROR_TITLE,
    errorDesc = message ?: Constant.ERROR_UNKNOWN,
    onErrorButtonClick = onRetry
)

@Composable
private fun RankingListPreviewContent(
    result: UiState<List<RankingUiModel>>,
    query: String = ""
) {
    AniMangaAnimeAndMangaInfoTheme {
        RankingListComponent(
            result = result,
            query = query,
            onCardClick = { _, _ -> },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RankingListComponentPreview() = RankingListPreviewContent(
    UiState.Success(listOf(dummyRankingUiModel, dummyRankingUiModel.copy(id = 1, rank = 2)))
)

@Preview(showBackground = true)
@Composable
private fun RankingListComponentHighlightPreview() = RankingListPreviewContent(
    UiState.Success(listOf(dummyRankingUiModel, dummyRankingUiModel.copy(id = 1, rank = 2))),
    query = "BLE"
)

@Preview(showBackground = true)
@Composable
private fun RankingListComponentLoadingPreview() = RankingListPreviewContent(UiState.Loading)

@Preview(showBackground = true)
@Composable
private fun RankingListComponentEmptyPreview() = RankingListPreviewContent(UiState.Empty)

@Preview(showBackground = true)
@Composable
private fun RankingListComponentEmptySearchPreview() =
    RankingListPreviewContent(UiState.Empty, query = "Zzz")

@Preview(showBackground = true)
@Composable
private fun RankingListComponentErrorPreview() = RankingListPreviewContent(
    UiState.ErrorException(message = "Something went wrong. Please try again.")
)
