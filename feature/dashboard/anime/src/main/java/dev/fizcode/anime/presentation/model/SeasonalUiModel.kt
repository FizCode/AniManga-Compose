package dev.fizcode.anime.presentation.model

internal data class SeasonalUiModel(
    val id: Int = 0,
    val mediaType: String,
    val title: String = "",
    val posterPath: String = "",
    val releaseInfo: String = "",
    val studio: List<String> = emptyList(),
    val synopsis: String = "",
    val rating: String = "",
    val genre: List<String> = emptyList()
)
