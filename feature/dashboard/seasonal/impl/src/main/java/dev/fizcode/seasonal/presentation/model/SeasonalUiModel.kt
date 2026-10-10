package dev.fizcode.seasonal.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class SeasonalUiModel(
    val id: Int,
    val rank: Int,
    val mediaType: String,
    val title: String,
    val posterPath: String,
    val rating: String,
    val releaseInfo: String,
    val studio: String,
    val synopsis: String,
    val genre: ImmutableList<String>
)

internal val dummySeasonalUiModel = SeasonalUiModel(
    id = 0,
    rank = 1,
    mediaType = "tv",
    posterPath = "",
    rating = "5.00",
    title = "BLEACH: Sennen Kessen-hen",
    releaseInfo = "Wednesday, 23:00 (JST)",
    studio = "Pierrot",
    synopsis = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
    genre = persistentListOf("Action", "Adventure", "Fantasy", "Comedy")
)
