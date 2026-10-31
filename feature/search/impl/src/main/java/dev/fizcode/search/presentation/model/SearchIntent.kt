package dev.fizcode.search.presentation.model

import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface SearchIntent : UiIntent {
    data class QueryChanged(val query: String) : SearchIntent
}
