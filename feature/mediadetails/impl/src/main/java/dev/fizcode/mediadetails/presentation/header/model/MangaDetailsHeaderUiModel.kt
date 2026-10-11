package dev.fizcode.mediadetails.presentation.header.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MangaDetailsHeaderUiModel(
    val pictures: ImmutableList<String>,
    val largePicture: ImmutableList<String>,
    val posterPath: String,
    val title: String,
    val mediaType: String,
    val serialization: String,
    /** E.g. `Vol. 74 Ch. 705 | Finished`. */
    val publishInfo: String,
    /** E.g. `Aug 7, 2001 to Aug 22, 2016`. */
    val publishedDates: String,
    val rank: String,
    val popularity: String,
    val members: String,
    val favorites: String,
    val score: String,
    val stars: Double,
    val totalVote: String,
    val genre: ImmutableList<String>
)
