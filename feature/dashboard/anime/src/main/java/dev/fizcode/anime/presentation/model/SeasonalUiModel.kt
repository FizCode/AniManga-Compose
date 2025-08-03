package dev.fizcode.anime.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class SeasonalUiModel(
    val id: Int,
    val mediaType: String,
    val title: String,
    val posterPath: String,
    val releaseInfo: String,
    val studio: String,
    val synopsis: String,
    val rating: String,
    val genre: ImmutableList<String>
)
