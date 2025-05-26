package dev.fizcode.mediadetails.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class JikanStaffResponse(
    @SerialName("data")
    val `data`: List<Data>? = null
) {
    @Serializable
    data class Data(
        @SerialName("person")
        val person: Person? = null,
        @SerialName("positions")
        val positions: List<String>? = null
    )

    @Serializable
    data class Person(
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
        val jpg: Jpg? = null
    )

    @Serializable
    data class Jpg(
        @SerialName("image_url")
        val imageUrl: String? = null
    )
}
