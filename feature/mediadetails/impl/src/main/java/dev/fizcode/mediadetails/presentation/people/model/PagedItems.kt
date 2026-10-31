package dev.fizcode.mediadetails.presentation.people.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class PagedItems<U>(
    val items: ImmutableList<U> = persistentListOf(),
    val hasNextPage: Boolean = false,
    val isLoadingMore: Boolean = false,
    val loadMoreFailed: Boolean = false
)
