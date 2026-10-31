package dev.fizcode.manga.presentation.model

import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface MangaIntent : UiIntent {
    data class SelectType(val type: MangaType) : MangaIntent
    data class SelectSort(val sort: MangaSort) : MangaIntent
    data class QueryChanged(val query: String) : MangaIntent
    data object Retry : MangaIntent
}
