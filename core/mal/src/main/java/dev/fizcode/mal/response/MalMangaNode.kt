package dev.fizcode.mal.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A manga as the list endpoints send it, every field is optional. */
@Serializable
data class MalMangaNode(
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
    @SerialName("num_volumes")
    val numVolumes: Int? = null,
    @SerialName("num_chapters")
    val numChapters: Int? = null,
    @SerialName("synopsis")
    val synopsis: String? = null,
    @SerialName("genres")
    val genres: List<MalGenre>? = null
)
