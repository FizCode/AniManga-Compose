package dev.fizcode.anime.presentation.model

internal data class TopRankingUiModel(
    val id: Int = 0,
    val mediaType: String = "",
    val posterPath: String = "",
    val rating: String = "",
    val title: String = "",
    val subTitle: String = "",
    val studio: String = "",
    val genre: List<String> = emptyList()
)
