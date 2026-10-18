package dev.fizcode.mediadetails.domain.model

/** One page of a list that is loaded page by page. */
internal data class PagedResult<T>(
    val items: List<T> = emptyList(),
    val hasNextPage: Boolean = false
)
