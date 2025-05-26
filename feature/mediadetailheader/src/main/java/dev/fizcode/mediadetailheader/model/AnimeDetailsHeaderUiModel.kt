package dev.fizcode.mediadetailheader.model

data class AnimeDetailsHeaderUiModel(
    val pictures: List<String> = emptyList(),
    val posterPath: String = "",
    val title: String = "",
    val mediaType: String = "",
    val releaseSeason: String = "",
    val studio: String = "",
    val releaseInfo: String = "",
    val duration: String = "",
    val rank: Int = 0,
    val popularity: Int = 0,
    val members: Int = 0,
    val favorites: Int = 0,
    val score: Double = 0.0,
    val totalVote: Int = 0,
    val genre: List<String> = emptyList()
)
