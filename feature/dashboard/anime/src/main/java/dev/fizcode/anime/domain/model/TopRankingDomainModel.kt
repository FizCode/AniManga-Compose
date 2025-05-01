package dev.fizcode.anime.domain.model

internal data class TopRankingDomainModel(
    val data: List<Data> = emptyList(),
    val paging: Paging = Paging()
) {
    data class Data(
        val node: Node = Node(),
        val ranking: Ranking = Ranking()
    )

    data class Node(
        val id: Int = 0,
        val title: String = "",
        val mainPicture: MainPicture = MainPicture(),
        val mean: Double = 0.0,
        val broadcast: Broadcast = Broadcast(),
        val status: String = "",
        val mediaType: String = "",
        val numEpisodes: Int = 0,
        val studios: List<Studio> = emptyList(),
        val genres: List<Genre> = emptyList()
    )

    data class MainPicture(
        val medium: String = "",
        val large: String = ""
    )

    data class Broadcast(
        val dayOfTheWeek: String = "",
        val startTime: String = ""
    )

    data class Studio(
        val id: Int = 0,
        val name: String = ""
    )

    data class Genre(
        val id: Int = 0,
        val name: String = ""
    )

    data class Ranking(
        val rank: Int = 0
    )

    data class Paging(
        val next: String = ""
    )
}