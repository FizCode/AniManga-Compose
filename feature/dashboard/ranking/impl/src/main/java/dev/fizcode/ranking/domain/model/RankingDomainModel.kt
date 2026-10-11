package dev.fizcode.ranking.domain.model

internal data class RankingDomainModel(
    val items: List<Item> = emptyList()
) {
    data class Item(
        val id: Int = 0,
        val rank: Int = 0,
        val mediaType: String = "",
        val title: String = "",
        val posterPath: String = "",
        val mean: Double = 0.0,
        val broadcastDay: String = "",
        val broadcastTime: String = "",
        val status: String = "",
        val numEpisodes: Int = 0,
        val studios: List<String> = emptyList(),
        val synopsis: String = "",
        val genres: List<String> = emptyList()
    )
}
