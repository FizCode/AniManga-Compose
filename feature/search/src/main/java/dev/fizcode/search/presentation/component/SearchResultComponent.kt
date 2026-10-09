package dev.fizcode.search.presentation.component

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
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.card.EmptyCardComponent
import dev.fizcode.designsystem.component.card.ErrorCardComponent
import dev.fizcode.designsystem.component.shimmer.MovieCardSmallShimmer
import dev.fizcode.search.presentation.model.SearchResultUiModel
import dev.fizcode.search.presentation.model.dummySearchResultUiModel
import dev.fizcode.search.util.Constant

@Composable
internal fun SearchResultComponent(
    result: UiState<List<SearchResultUiModel>>,
    query: String,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    modifier: Modifier = Modifier
) = when (result) {
    is UiState.Loading -> Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(SHIMMER_COUNT) { MovieCardSmallShimmer() }
    }

    is UiState.Success -> LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = result.data, key = { it.id }) { item ->
            SearchResultItemComponent(
                item = item,
                query = query,
                onCardClick = { onCardClick(item.mediaType, item.id) }
            )
        }
    }

    is UiState.Empty -> EmptyCardComponent(
        modifier = modifier,
        desc = if (query.length < Constant.MIN_QUERY_LENGTH) {
            Constant.EMPTY_QUERY
        } else {
            Constant.EMPTY_RESULT
        }
    )

    is UiState.ErrorRedirectResponse -> SearchErrorComponent(result.message, modifier)
    is UiState.ErrorClientRequest -> SearchErrorComponent(result.message, modifier)
    is UiState.ErrorServerResponse -> SearchErrorComponent(result.message, modifier)
    is UiState.ErrorException -> SearchErrorComponent(result.message, modifier)
}

@Composable
private fun SearchErrorComponent(
    message: String?,
    modifier: Modifier
) = ErrorCardComponent(
    modifier = modifier,
    errorCode = Constant.ERROR_TITLE,
    errorDesc = message ?: Constant.ERROR_UNKNOWN
)

private const val SHIMMER_COUNT = 5

@Preview(showBackground = true)
@Composable
private fun SearchResultComponentPreview() {
    SearchResultComponent(
        result = UiState.Success(listOf(dummySearchResultUiModel)),
        query = "Ble",
        onCardClick = { _, _ -> }
    )
}
