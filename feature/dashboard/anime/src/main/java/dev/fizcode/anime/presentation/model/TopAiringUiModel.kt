package dev.fizcode.anime.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class TopAiringUiModel(
    val id: Int = 0,
    val mediaType: String = "",
    val posterPath: String = "",
    val rating: String = "",
    val title: String = ""
)
