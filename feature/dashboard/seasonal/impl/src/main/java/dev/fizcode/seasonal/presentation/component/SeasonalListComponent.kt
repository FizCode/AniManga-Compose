package dev.fizcode.seasonal.presentation.component

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
import dev.fizcode.seasonal.presentation.model.SeasonalUiModel
import dev.fizcode.seasonal.presentation.model.dummySeasonalUiModel
import dev.fizcode.seasonal.util.Constant

@Composable
internal fun SeasonalListComponent(
    result: UiState<List<SeasonalUiModel>>,
    isFiltering: Boolean,
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
        desc = if (isFiltering) Constant.EMPTY_QUERY_RESULT else Constant.EMPTY_RESULT
    )

    is UiState.ErrorRedirectResponse -> SeasonalError(result.message, onRetry, modifier)
    is UiState.ErrorClientRequest -> SeasonalError(result.message, onRetry, modifier)
    is UiState.ErrorServerResponse -> SeasonalError(result.message, onRetry, modifier)
    is UiState.ErrorException -> SeasonalError(result.message, onRetry, modifier)
}

@Composable
private fun SeasonalError(
    message: String?,
    onRetry: () -> Unit,
    modifier: Modifier
) = ErrorCardWithButtonComponent(
    modifier = modifier,
    errorCode = Constant.ERROR_TITLE,
    errorDesc = message ?: Constant.ERROR_UNKNOWN,
    onErrorButtonClick = onRetry
)

@Preview(showBackground = true)
@Composable
private fun SeasonalListComponentPreview() {
    SeasonalListComponent(
        result = UiState.Success(listOf(dummySeasonalUiModel, dummySeasonalUiModel.copy(id = 1, rank = 2))),
        isFiltering = false,
        onCardClick = { _, _ -> },
        onRetry = {}
    )
}
