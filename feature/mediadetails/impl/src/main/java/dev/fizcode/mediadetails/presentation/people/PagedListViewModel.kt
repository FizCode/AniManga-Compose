package dev.fizcode.mediadetails.presentation.people

import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.callhandler.catchAsNetworkError
import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.common.base.presentationhandler.toUiState
import dev.fizcode.mediadetails.domain.model.PagedResult
import dev.fizcode.mediadetails.presentation.people.model.PagedItems
import dev.fizcode.mediadetails.presentation.people.model.PagedListIntent
import dev.fizcode.mediadetails.presentation.people.model.PagedListUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

/**
 * Loads a list page by page. The first page is a [UiState] of its own (loading, empty, error),
 * the pages after it are added to the list as the user scrolls, see [PagedListIntent.LoadMore].
 *
 * @param fetchPage loads one page, the first page is 1.
 * @param mapPage turns the items of one page into what the screen shows.
 */
internal abstract class PagedListViewModel<D, U>(
    private val fetchPage: suspend (page: Int) -> DomainNetworkState<PagedResult<D>>,
    private val mapPage: (List<D>) -> List<U>
) : MviViewModel<PagedListUiState<U>, PagedListIntent, Nothing>(PagedListUiState()) {

    private var nextPage = FIRST_PAGE

    init {
        loadFirstPage()
    }

    override fun onIntent(intent: PagedListIntent) {
        when (intent) {
            PagedListIntent.LoadMore -> loadMore()
            PagedListIntent.Retry -> if (uiState.value.items is UiState.Success) loadMore() else loadFirstPage()
        }
    }

    private fun loadMore() {
        val current = (uiState.value.items as? UiState.Success)?.data ?: return
        if (!current.hasNextPage || current.isLoadingMore) return

        setItems(current.copy(isLoadingMore = true, loadMoreFailed = false))
        viewModelScope.launch {
            when (val result = catchAsNetworkError { fetchPage(nextPage) }) {
                is DomainNetworkState.Success -> {
                    nextPage++
                    setItems(
                        PagedItems(
                            items = (current.items + mapPage(result.data.items)).toImmutableList(),
                            hasNextPage = result.data.hasNextPage
                        )
                    )
                }

                is DomainNetworkState.Empty ->
                    setItems(current.copy(hasNextPage = false, isLoadingMore = false))

                is DomainNetworkState.ErrorNetwork ->
                    setItems(current.copy(isLoadingMore = false, loadMoreFailed = true))
            }
        }
    }

    private fun loadFirstPage() {
        setState { copy(items = UiState.Loading) }
        viewModelScope.launch {
            val result = catchAsNetworkError { fetchPage(FIRST_PAGE) }
            nextPage = FIRST_PAGE + 1
            val state = result.toUiState { page ->
                PagedItems(
                    items = mapPage(page.items).toImmutableList(),
                    hasNextPage = page.hasNextPage
                )
            }
            setState {
                copy(
                    items = if (state is UiState.Success && state.data.items.isEmpty()) UiState.Empty else state
                )
            }
        }
    }

    private fun setItems(items: PagedItems<U>) = setState { copy(items = UiState.Success(items)) }

    internal companion object {
        const val FIRST_PAGE = 1

        const val PAGE_SIZE = 50
    }
}
