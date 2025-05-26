package dev.fizcode.mediadetails.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class JikanVoiceActorsResponse(
    @SerialName("data")
    val `data`: List<Data>? = null
) {
    @Serializable
    data class Data(
        @SerialName("character")
        val character: Character? = null,
        @SerialName("favorites")
        val favorites: Int? = null,
        @SerialName("role")
        val role: String? = null,
        @SerialName("voice_actors")
        val voiceActors: List<VoiceActor>? = null
    )

    @Serializable
    data class Character(
        @SerialName("images")
        val images: Images? = null,
        @SerialName("mal_id")
        val malId: Int? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("url")
        val url: String? = null
    )

    @Serializable
    data class Images(
        @SerialName("jpg")
        val jpg: Jpg? = null,
        @SerialName("webp")
        val webp: Webp? = null
    )

    @Serializable
    data class Jpg(
        @SerialName("image_url")
        val imageUrl: String? = null
    )

    @Serializable
    data class Webp(
        @SerialName("image_url")
        val imageUrl: String? = null,
        @SerialName("small_image_url")
        val smallImageUrl: String? = null
    )

    @Serializable
    data class VoiceActor(
        @SerialName("language")
        val language: String? = null,
        @SerialName("person")
        val person: Person? = null
    )

    @Serializable
    data class Person(
        @SerialName("images")
        val images: PersonImages? = null,
        @SerialName("mal_id")
        val malId: Int? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("url")
        val url: String? = null
    )

    @Serializable
    data class PersonImages(
        @SerialName("jpg")
        val jpg: PersonImagesJpg? = null
    )

    @Serializable
    data class PersonImagesJpg(
        @SerialName("image_url")
        val imageUrl: String? = null
    )
}
