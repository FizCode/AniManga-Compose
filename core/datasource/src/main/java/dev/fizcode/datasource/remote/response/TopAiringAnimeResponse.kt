package dev.fizcode.datasource.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopAiringAnimeResponse(
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
        val mean: Double? = null
    )

    @Serializable
    data class MainPicture(
        @SerialName("medium")
        val medium: String? = null,
        @SerialName("large")
        val large: String? = null
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
