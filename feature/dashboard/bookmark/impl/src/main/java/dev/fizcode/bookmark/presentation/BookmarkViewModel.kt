package dev.fizcode.bookmark.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.bookmark.domain.repository.BookmarkRepository
import dev.fizcode.bookmark.presentation.mapper.BookmarkUiMapper
import dev.fizcode.bookmark.presentation.model.BookmarkTab
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.asUiStateFlowDbCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

internal class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository,
    private val bookmarkUiMapper: BookmarkUiMapper
) : ViewModel() {

    val bookmarks: StateFlow<UiState<List<BookmarkUiModel>>> = asUiStateFlowDbCall(
        scope = viewModelScope,
        domain = { bookmarkRepository.getAllBookmarks() },
        mapper = { data -> bookmarkUiMapper.mapToBookmarkUiModel(data) }
    )

    /** Local title filter applied on top of the bookmarks. */
    val queryState = TextFieldState()

    private val _selectedTab = MutableStateFlow(BookmarkTab.ANIME)
    val selectedTab: StateFlow<BookmarkTab> = _selectedTab.asStateFlow()

    fun selectTab(tab: BookmarkTab) {
        _selectedTab.value = tab
    }

    /** [bookmarks] narrowed by the selected tab and the title typed in [queryState]. */
    val visibleBookmarks: StateFlow<UiState<List<BookmarkUiModel>>> = combine(
        bookmarks,
        _selectedTab,
        snapshotFlow { queryState.text.toString().trim() }.distinctUntilChanged()
    ) { state, tab, query ->
        if (state is UiState.Success) {
            state.data
                .filter { tab.matches(it.mediaType) && it.titles.matches(query) }
                .let { if (it.isEmpty()) UiState.Empty else UiState.Success(it) }
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
}
