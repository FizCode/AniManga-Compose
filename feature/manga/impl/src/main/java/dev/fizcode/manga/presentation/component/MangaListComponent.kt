package dev.fizcode.manga.presentation.component

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
import dev.fizcode.manga.presentation.model.MangaUiModel
import dev.fizcode.manga.presentation.model.dummyMangaUiModel
import dev.fizcode.manga.util.Constant
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.common.settings.LocalTitleStyle

@Composable
internal fun MangaListComponent(
    result: UiState<List<MangaUiModel>>,
    isFiltering: Boolean,
    onRetry: () -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
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
                title = item.titles.resolve(LocalTitleStyle.current),
                subTitle = item.subTitle,
                studio = item.mediaTypeLabel,
                synopsis = item.synopsis,
                genre = item.genre,
                onCardClick = { onCardClick(item.mediaType, item.id) }
            )
        }
    }

    is UiState.Empty -> EmptyCardComponent(
        modifier = modifier,
        desc = if (isFiltering) Constant.EMPTY_QUERY_RESULT else Constant.EMPTY_RESULT
    )

    is UiState.ErrorRedirectResponse -> MangaError(result.message, onRetry, modifier)
    is UiState.ErrorClientRequest -> MangaError(result.message, onRetry, modifier)
    is UiState.ErrorServerResponse -> MangaError(result.message, onRetry, modifier)
    is UiState.ErrorException -> MangaError(result.message, onRetry, modifier)
}

@Composable
private fun MangaError(
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
private fun MangaListPreviewContent(
    result: UiState<List<MangaUiModel>>,
    isFiltering: Boolean = false
) {
    AniMangaAnimeAndMangaInfoTheme {
        MangaListComponent(
            result = result,
            isFiltering = isFiltering,
            onRetry = {},
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaListComponentPreview() = MangaListPreviewContent(
    UiState.Success(listOf(dummyMangaUiModel, dummyMangaUiModel.copy(id = 1)))
)

@Preview(showBackground = true)
@Composable
private fun MangaListComponentLoadingPreview() = MangaListPreviewContent(UiState.Loading)

@Preview(showBackground = true)
@Composable
private fun MangaListComponentEmptyPreview() = MangaListPreviewContent(UiState.Empty)

@Preview(showBackground = true)
@Composable
private fun MangaListComponentEmptyFilterPreview() =
    MangaListPreviewContent(UiState.Empty, isFiltering = true)

@Preview(showBackground = true)
@Composable
private fun MangaListComponentErrorPreview() = MangaListPreviewContent(UiState.ErrorException(message = "Something went wrong. Please try again."))
