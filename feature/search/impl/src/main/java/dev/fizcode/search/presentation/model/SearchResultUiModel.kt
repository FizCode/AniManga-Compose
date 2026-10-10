package dev.fizcode.search.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class SearchResultUiModel(
    val id: Int,
    val mediaType: String,
    val posterPath: String,
    val rating: String,
    val title: String,
    val subTitle: String,
    val genre: ImmutableList<String>
)

internal val dummySearchResultUiModel = SearchResultUiModel(
    id = 0,
    mediaType = "tv",
    posterPath = "",
    rating = "5.00",
    title = "BLEACH: Sennen Kessen-hen",
    subTitle = "TV | 12 Episodes | Finished Airing",
    genre = persistentListOf("Action", "Adventure", "Fantasy", "Comedy")
)
