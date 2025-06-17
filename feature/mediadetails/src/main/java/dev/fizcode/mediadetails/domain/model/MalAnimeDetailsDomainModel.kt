package dev.fizcode.mediadetails.domain.model

internal data class MalAnimeDetailsDomainModel(
    val alternativeTitles: AlternativeTitles,
    val averageEpisodeDuration: Int = 0,
    val background: String = "",
    val broadcast: Broadcast,
    val endDate: String = "",
    val genres: List<Genre> = emptyList(),
    val id: Int = 0,
    val mainPicture: MainPicture,
    val mean: Double = 0.0,
    val mediaType: String = "",
    val numEpisodes: Int = 0,
    val numListUsers: Int = 0,
    val numScoringUsers: Int = 0,
    val pictures: List<Picture> = emptyList(),
    val popularity: Int = 0,
    val rank: Int = 0,
    val rating: String = "",
    val source: String = "",
    val startDate: String = "",
    val startSeason: StartSeason,
    val status: String = "",
    val studios: List<Studio> = emptyList(),
    val synopsis: String = "",
    val title: String = ""
)

internal data class AlternativeTitles(
    val en: String = "",
    val ja: String = "",
    val synonyms: List<String> = emptyList()
)

internal data class Broadcast(
    val dayOfTheWeek: String = "",
    val startTime: String = ""
)

internal data class Genre(
    val id: Int = 0,
    val name: String = ""
)

internal data class MainPicture(
    val large: String = "",
    val medium: String = ""
)

internal data class Picture(
    val large: String = "",
    val medium: String = ""
)

internal data class StartSeason(
    val season: String = "",
    val year: Int = 0
)

internal data class Studio(
    val id: Int = 0,
    val name: String = ""
)
