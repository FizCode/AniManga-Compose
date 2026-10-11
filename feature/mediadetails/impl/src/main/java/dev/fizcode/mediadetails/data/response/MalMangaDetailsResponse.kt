package dev.fizcode.mediadetails.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MalMangaDetailsResponse(
    @SerialName("alternative_titles")
    val alternativeTitles: MalAnimeDetailsResponse.AlternativeTitles? = null,
    @SerialName("authors")
    val authors: List<Author>? = null,
    @SerialName("background")
    val background: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("genres")
    val genres: List<MalAnimeDetailsResponse.Genre>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("main_picture")
    val mainPicture: MalAnimeDetailsResponse.MainPicture? = null,
    @SerialName("mean")
    val mean: Double? = null,
    @SerialName("media_type")
    val mediaType: String? = null,
    @SerialName("num_chapters")
    val numChapters: Int? = null,
    @SerialName("num_list_users")
    val numListUsers: Int? = null,
    @SerialName("num_scoring_users")
    val numScoringUsers: Int? = null,
    @SerialName("num_volumes")
    val numVolumes: Int? = null,
    @SerialName("pictures")
    val pictures: List<MalAnimeDetailsResponse.Picture>? = null,
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("rank")
    val rank: Int? = null,
    @SerialName("related_manga")
    val relatedManga: List<MalAnimeDetailsResponse.RelatedAnime>? = null,
    @SerialName("serialization")
    val serialization: List<Serialization>? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("synopsis")
    val synopsis: String? = null,
    @SerialName("title")
    val title: String? = null
) {
    @Serializable
    data class Author(
        @SerialName("node")
        val node: AuthorNode? = null,
        @SerialName("role")
        val role: String? = null
    )

    @Serializable
    data class AuthorNode(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("first_name")
        val firstName: String? = null,
        @SerialName("last_name")
        val lastName: String? = null
    )

    @Serializable
    data class Serialization(
        @SerialName("node")
        val node: SerializationNode? = null
    )

    @Serializable
    data class SerializationNode(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null
    )
}
