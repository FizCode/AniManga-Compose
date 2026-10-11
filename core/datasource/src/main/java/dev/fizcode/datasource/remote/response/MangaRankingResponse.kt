package dev.fizcode.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaRankingResponse(
    @SerialName("data")
    val data: List<Data>? = null,
    @SerialName("paging")
    val paging: Paging? = null
) {
    @Serializable
    data class Data(
        @SerialName("node")
        val node: Node? = null,
        @SerialName("ranking")
        val ranking: Ranking? = null
    )

    @Serializable
    data class Node(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("alternative_titles")
        val alternativeTitles: AlternativeTitlesResponse? = null,
        @SerialName("nsfw")
        val nsfw: String? = null,
        @SerialName("main_picture")
        val mainPicture: MainPicture? = null,
        @SerialName("mean")
        val mean: Double? = null,
        @SerialName("status")
        val status: String? = null,
        @SerialName("media_type")
        val mediaType: String? = null,
        @SerialName("num_volumes")
        val numVolumes: Int? = null,
        @SerialName("num_chapters")
        val numChapters: Int? = null,
        @SerialName("synopsis")
        val synopsis: String? = null,
        @SerialName("genres")
        val genres: List<Genre>? = null
    )

    @Serializable
    data class MainPicture(
        @SerialName("medium")
        val medium: String? = null,
        @SerialName("large")
        val large: String? = null
    )

    @Serializable
    data class Genre(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null
    )

    @Serializable
    data class Ranking(
        @SerialName("rank")
        val rank: Int? = null
    )

    @Serializable
    data class Paging(
        @SerialName("next")
        val next: String? = null
    )
}
