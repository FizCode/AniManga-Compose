package dev.fizcode.mediadetails.presentation.people

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.toUiState
import dev.fizcode.mediadetails.domain.model.PagedResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** The items loaded so far of a list that is loaded page by page. */
@Immutable
internal data class PagedItems<U>(
    val items: ImmutableList<U> = persistentListOf(),
    val hasNextPage: Boolean = false,
    val isLoadingMore: Boolean = false,
    val loadMoreFailed: Boolean = false
)

/**
 * Loads a list page by page. The first page is a [UiState] of its own (loading, empty, error),
 * the pages after it are added to the list as the user scrolls, see [loadMore].
 *
 * @param fetchPage loads one page, the first page is 1.
 * @param mapPage turns the items of one page into what the screen shows.
 */
internal abstract class PagedListViewModel<D, U>(
    private val fetchPage: suspend (page: Int) -> DomainNetworkState<PagedResult<D>>,
    private val mapPage: (List<D>) -> List<U>
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<PagedItems<U>>>(UiState.Loading)
    val state: StateFlow<UiState<PagedItems<U>>> = _state.asStateFlow()

    private var nextPage = FIRST_PAGE

    init {
        loadFirstPage()
    }

    /** Tries again after a failure, the first page or the page that did not load. */
    fun retry() {
        if (_state.value is UiState.Success) loadMore() else loadFirstPage()
    }

    /** Loads the next page, nothing happens while one is loading or when there is no more. */
    fun loadMore() {
        val current = (_state.value as? UiState.Success)?.data ?: return
        if (!current.hasNextPage || current.isLoadingMore) return

        _state.value = UiState.Success(current.copy(isLoadingMore = true, loadMoreFailed = false))
        viewModelScope.launch {
            when (val result = fetch(nextPage)) {
                is DomainNetworkState.Success -> {
                    nextPage++
                    _state.value = UiState.Success(
                        PagedItems(
                            items = (current.items + mapPage(result.data.items)).toImmutableList(),
                            hasNextPage = result.data.hasNextPage
                        )
                    )
                }

                is DomainNetworkState.Empty -> _state.value = UiState.Success(
                    current.copy(hasNextPage = false, isLoadingMore = false)
                )

                is DomainNetworkState.ErrorNetwork -> _state.value = UiState.Success(
                    current.copy(isLoadingMore = false, loadMoreFailed = true)
                )
            }
        }
    }

    private fun loadFirstPage() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = fetch(FIRST_PAGE)
            nextPage = FIRST_PAGE + 1
            _state.value = result.toUiState { page ->
                PagedItems(
                    items = mapPage(page.items).toImmutableList(),
                    hasNextPage = page.hasNextPage
                )
            }.let { state ->
                // A first page without items is an empty list, not a list to show.
                if (state is UiState.Success && state.data.items.isEmpty()) UiState.Empty else state
            }
        }
    }

    private suspend fun fetch(page: Int): DomainNetworkState<PagedResult<D>> =
        withContext(Dispatchers.IO) {
            try {
                fetchPage(page)
            } catch (e: Exception) {
                DomainNetworkState.ErrorNetwork(exception = e, message = e.message)
            }
        }

    internal companion object {
        const val FIRST_PAGE = 1

        /** The most AniList sends in one page. */
        const val PAGE_SIZE = 50
    }
}
