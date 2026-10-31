package dev.fizcode.bookmark.presentation.model

import dev.fizcode.bookmark.presentation.model.BookmarkTab
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.common.base.callhandler.UiState

internal data class BookmarkUiState(
    val tab: BookmarkTab = BookmarkTab.ANIME,
    val query: String = "",
    val allBookmarks: UiState<List<BookmarkUiModel>> = UiState.Loading
) {
    val bookmarks: UiState<List<BookmarkUiModel>> = when (allBookmarks) {
        is UiState.Success -> allBookmarks.data
            .filter { tab.matches(it.mediaType) && it.titles.matches(query) }
            .let { if (it.isEmpty()) UiState.Empty else UiState.Success(it) }
        else -> allBookmarks
    }
}
