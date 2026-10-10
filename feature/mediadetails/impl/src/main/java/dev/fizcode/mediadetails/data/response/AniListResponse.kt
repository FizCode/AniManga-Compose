package dev.fizcode.mediadetails.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A character or a person (voice actor, staff member) of an AniList media. */
@Serializable
internal data class AniListPerson(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("siteUrl")
    val siteUrl: String? = null,
    @SerialName("name")
    val name: Name? = null,
    @SerialName("image")
    val image: Image? = null
) {
    @Serializable
    data class Name(
        @SerialName("full")
        val full: String? = null
    )

    @Serializable
    data class Image(
        @SerialName("medium")
        val medium: String? = null
    )
}

/** Characters with their Japanese voice actors, from the AniList `Media.characters` field. */
@Serializable
internal data class AniListCastResponse(
    @SerialName("data")
    val data: Data? = null
) {
    @Serializable
    data class Data(
        @SerialName("Media")
        val media: Media? = null
    )

    @Serializable
    data class Media(
        @SerialName("characters")
        val characters: Characters? = null
    )

    @Serializable
    data class Characters(
        @SerialName("edges")
        val edges: List<Edge>? = null
    )

    @Serializable
    data class Edge(
        @SerialName("role")
        val role: String? = null,
        @SerialName("node")
        val node: AniListPerson? = null,
        @SerialName("voiceActors")
        val voiceActors: List<AniListPerson>? = null
    )
}

/** Staff members with their role, from the AniList `Media.staff` field. */
@Serializable
internal data class AniListStaffResponse(
    @SerialName("data")
    val data: Data? = null
) {
    @Serializable
    data class Data(
        @SerialName("Media")
        val media: Media? = null
    )

    @Serializable
    data class Media(
        @SerialName("staff")
        val staff: Staff? = null
    )

    @Serializable
    data class Staff(
        @SerialName("edges")
        val edges: List<Edge>? = null
    )

    @Serializable
    data class Edge(
        @SerialName("role")
        val role: String? = null,
        @SerialName("node")
        val node: AniListPerson? = null
    )
}
