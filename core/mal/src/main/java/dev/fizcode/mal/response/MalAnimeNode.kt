package dev.fizcode.mal.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** An anime as the list endpoints send it, every field is optional. */
@Serializable
data class MalAnimeNode(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("alternative_titles")
    val alternativeTitles: MalAlternativeTitles? = null,
    /** `white`, `gray` or `black`. */
    @SerialName("nsfw")
    val nsfw: String? = null,
    @SerialName("main_picture")
    val mainPicture: MalPicture? = null,
    @SerialName("mean")
    val mean: Double? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("media_type")
    val mediaType: String? = null,
    @SerialName("num_episodes")
    val numEpisodes: Int? = null,
    @SerialName("studios")
    val studios: List<MalStudio>? = null,
    @SerialName("synopsis")
    val synopsis: String? = null,
    @SerialName("genres")
    val genres: List<MalGenre>? = null,
    @SerialName("broadcast")
    val broadcast: MalBroadcast? = null
)

@Serializable
data class MalStudio(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
)

@Serializable
data class MalBroadcast(
    @SerialName("day_of_the_week")
    val dayOfTheWeek: String? = null,
    @SerialName("start_time")
    val startTime: String? = null
)
