package dev.fizcode.mediadetails.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MalAnimeDetailsResponse(
    @SerialName("alternative_titles")
    val alternativeTitles: AlternativeTitles? = null,
    @SerialName("average_episode_duration")
    val averageEpisodeDuration: Int? = null,
    @SerialName("background")
    val background: String? = null,
    @SerialName("broadcast")
    val broadcast: Broadcast? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("genres")
    val genres: List<Genre>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("main_picture")
    val mainPicture: MainPicture? = null,
    @SerialName("mean")
    val mean: Double? = null,
    @SerialName("media_type")
    val mediaType: String? = null,
    @SerialName("num_episodes")
    val numEpisodes: Int? = null,
    @SerialName("num_list_users")
    val numListUsers: Int? = null,
    @SerialName("num_scoring_users")
    val numScoringUsers: Int? = null,
    @SerialName("pictures")
    val pictures: List<Picture>? = null,
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("rank")
    val rank: Int? = null,
    @SerialName("related_anime")
    val relatedAnime: List<RelatedAnime>? = null,
    @SerialName("rating")
    val rating: String? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("start_season")
    val startSeason: StartSeason? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("studios")
    val studios: List<Studio>? = null,
    @SerialName("synopsis")
    val synopsis: String? = null,
    @SerialName("title")
    val title: String? = null
) {
    @Serializable
    data class AlternativeTitles(
        @SerialName("en")
        val en: String? = null,
        @SerialName("ja")
        val ja: String? = null,
        @SerialName("synonyms")
        val synonyms: List<String>? = null
    )

    @Serializable
    data class Broadcast(
        @SerialName("day_of_the_week")
        val dayOfTheWeek: String? = null,
        @SerialName("start_time")
        val startTime: String? = null
    )

    @Serializable
    data class Genre(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null
    )

    @Serializable
    data class MainPicture(
        @SerialName("large")
        val large: String? = null,
        @SerialName("medium")
        val medium: String? = null
    )

    @Serializable
    data class Picture(
        @SerialName("large")
        val large: String? = null,
        @SerialName("medium")
        val medium: String? = null
    )

    @Serializable
    data class RelatedAnime(
        @SerialName("node")
        val node: Node? = null,
        @SerialName("relation_type")
        val relationType: String? = null,
        @SerialName("relation_type_formatted")
        val relationTypeFormatted: String? = null
    )

    @Serializable
    data class Node(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("main_picture")
        val mainPicture: MainPicture? = null,
        @SerialName("title")
        val title: String? = null
    )

    @Serializable
    data class StartSeason(
        @SerialName("season")
        val season: String? = null,
        @SerialName("year")
        val year: Int? = null
    )

    @Serializable
    data class Studio(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null
    )
}
