package dev.fizcode.mediadetailheader.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AnimeDetailsHeaderUiModel(
    val pictures: ImmutableList<String>,
    val largePicture: ImmutableList<String>,
    val posterPath: String,
    val title: String,
    val mediaType: String,
    val releaseSeason: String,
    val studio: String,
    val releaseInfo: String,
    val duration: String,
    val rank: String,
    val popularity: String,
    val members: String,
    val favorites: String,
    val score: String,
    val stars: Double,
    val totalVote: String,
    val genre: ImmutableList<String>
)
