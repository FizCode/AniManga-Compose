package dev.fizcode.bookmark.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.bookmark.domain.repository.BookmarkRepository
import dev.fizcode.bookmark.presentation.mapper.BookmarkUiMapper
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.asStateFlowDbCall
import kotlinx.coroutines.flow.StateFlow

internal class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository,
    private val bookmarkUiMapper: BookmarkUiMapper
) : ViewModel() {

    val bookmarks: StateFlow<UiState<List<BookmarkUiModel>>> = asStateFlowDbCall(
        scope = viewModelScope,
        domain = { bookmarkRepository.getAllBookmarks() },
        mapper = { data -> bookmarkUiMapper.mapToBookmarkUiModel(data) }
    )
}
