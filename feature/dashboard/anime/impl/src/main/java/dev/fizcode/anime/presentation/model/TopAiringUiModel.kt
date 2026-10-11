package dev.fizcode.anime.presentation.model

import androidx.compose.runtime.Immutable
import dev.fizcode.common.settings.MediaTitles

@Immutable
internal data class TopAiringUiModel(
    val id: Int,
    val mediaType: String,
    val posterPath: String,
    val rating: String,
    val title: String,
    val titles: MediaTitles = MediaTitles(title)
)
