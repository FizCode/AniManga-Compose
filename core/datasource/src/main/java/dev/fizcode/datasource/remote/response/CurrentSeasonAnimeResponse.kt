package dev.fizcode.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentSeasonAnimeResponse(
    @SerialName("data")
    val data: List<Data>? = null,
    @SerialName("paging")
    val paging: Paging? = null,
    @SerialName("season")
    val season: Season? = null
) {
    @Serializable
    data class Data(
        @SerialName("node")
        val node: Node? = null
    )

    @Serializable
    data class Node(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("main_picture")
        val mainPicture: MainPicture? = null,
        @SerialName("mean")
        val mean: Double? = null,
        @SerialName("status")
        val status: String? = null,
        @SerialName("media_type")
        val mediaType: String? = null,
        @SerialName("num_episodes")
        val numEpisodes: Int? = null,
        @SerialName("studios")
        val studios: List<Studio>? = null,
        @SerialName("synopsis")
        val synopsis: String? = null,
        @SerialName("genres")
        val genres: List<Genre>? = null,
        @SerialName("broadcast")
        val broadcast: Broadcast? = null
    )

    @Serializable
    data class MainPicture(
        @SerialName("medium")
        val medium: String? = null,
        @SerialName("large")
        val large: String? = null
    )

    @Serializable
    data class Studio(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null
    )

    @Serializable
    data class Genre(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null
    )

    @Serializable
    data class Broadcast(
        @SerialName("day_of_the_week")
        val dayOfTheWeek: String? = null,
        @SerialName("start_time")
        val startTime: String? = null
    )

    @Serializable
    data class Paging(
        @SerialName("next")
        val next: String? = null
    )

    @Serializable
    data class Season(
        @SerialName("year")
        val year: Int? = null,
        @SerialName("season")
        val season: String? = null
    )
}