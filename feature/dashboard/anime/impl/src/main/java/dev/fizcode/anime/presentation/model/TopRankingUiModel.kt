package dev.fizcode.anime.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import dev.fizcode.common.settings.MediaTitles

@Immutable
internal data class TopRankingUiModel(
    val id: Int,
    val mediaType: String,
    val posterPath: String,
    val rating: String,
    val title: String,
    val subTitle: String,
    val studio: String,
    val genre: ImmutableList<String>,
    val titles: MediaTitles = MediaTitles(title)
)
