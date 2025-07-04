package dev.fizcode.mediadetailheader.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AnimeDetailsHeaderUiModel(
    val pictures: ImmutableList<String> = persistentListOf(),
    val largePicture: ImmutableList<String> = persistentListOf(),
    val posterPath: String = "",
    val title: String = "",
    val mediaType: String = "",
    val releaseSeason: String = "",
    val studio: String = "",
    val releaseInfo: String = "",
    val duration: String = "",
    val rank: String = "",
    val popularity: String = "",
    val members: String = "",
    val favorites: String = "",
    val score: String = "",
    val stars: Double = 0.0,
    val totalVote: String = "",
    val genre: ImmutableList<String> = persistentListOf()
)
