package dev.fizcode.anime.domain.model

internal data class SeasonalAnimeDomainModel(
    val data: List<Data> = emptyList(),
    val paging: Paging = Paging(),
    val season: Season = Season()
)

internal data class Data(
    val node: Node = Node()
)

internal data class Node(
    val id: Int = 0,
    val mediaType: String = "",
    val title: String = "",
    val mainPicture: MainPicture = MainPicture(),
    val mean: Double = 0.0,
    val broadcast: Broadcast = Broadcast(),
    val status: String = "",
    val numEpisodes: Int = 0,
    val studios: List<Studio> = emptyList(),
    val synopsis: String = "",
    val genres: List<Genre> = emptyList()
)

internal data class MainPicture(
    val medium: String = "",
    val large: String = ""
)

internal data class Broadcast(
    val dayOfTheWeek: String = "",
    val startTime: String = ""
)

internal data class Studio(
    val id: Int = 0,
    val name: String = ""
)

internal data class Genre(
    val id: Int = 0,
    val name: String = ""
)

internal data class Paging(
    val next: String = ""
)

internal data class Season(
    val year: Int = 0,
    val season: String = ""
)
