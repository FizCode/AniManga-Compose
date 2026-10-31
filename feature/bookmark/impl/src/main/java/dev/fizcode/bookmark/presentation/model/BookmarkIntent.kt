package dev.fizcode.bookmark.presentation.model

import dev.fizcode.bookmark.presentation.model.BookmarkTab
import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface BookmarkIntent : UiIntent {
    data class SelectTab(val tab: BookmarkTab) : BookmarkIntent
    data class QueryChanged(val query: String) : BookmarkIntent
}
