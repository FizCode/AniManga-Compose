package dev.fizcode.bookmark.presentation

import dev.fizcode.bookmark.domain.repository.BookmarkRepository
import dev.fizcode.bookmark.presentation.mapper.BookmarkUiMapper
import dev.fizcode.bookmark.presentation.model.BookmarkIntent
import dev.fizcode.bookmark.presentation.model.BookmarkUiState
import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.common.base.presentationhandler.asUiState

internal class BookmarkViewModel(
    bookmarkRepository: BookmarkRepository,
    bookmarkUiMapper: BookmarkUiMapper
) : MviViewModel<BookmarkUiState, BookmarkIntent, Nothing>(BookmarkUiState()) {

    init {
        bookmarkRepository.getAllBookmarks()
            .asUiState(bookmarkUiMapper::mapToBookmarkUiModel)
            .collectIntoState { copy(allBookmarks = it) }
    }

    override fun onIntent(intent: BookmarkIntent) = when (intent) {
        is BookmarkIntent.SelectTab -> setState { copy(tab = intent.tab) }
        is BookmarkIntent.QueryChanged -> setState { copy(query = intent.query.trim()) }
    }
}
