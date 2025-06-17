package dev.fizcode.anime.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class TopRankingUiModel(
    val id: Int = 0,
    val mediaType: String = "",
    val posterPath: String = "",
    val rating: String = "",
    val title: String = "",
    val subTitle: String = "",
    val studio: String = "",
    val genre: ImmutableList<String> = persistentListOf()
)
