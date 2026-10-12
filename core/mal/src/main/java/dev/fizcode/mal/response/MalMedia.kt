package dev.fizcode.mal.response

import dev.fizcode.common.settings.AlternativeTitles
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalPicture(
    @SerialName("medium")
    val medium: String? = null,
    @SerialName("large")
    val large: String? = null
)

@Serializable
data class MalGenre(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
)

/** The `alternative_titles` field of a media, only sent when it is asked for. */
@Serializable
data class MalAlternativeTitles(
    @SerialName("synonyms")
    val synonyms: List<String>? = null,
    @SerialName("en")
    val en: String? = null,
    @SerialName("ja")
    val ja: String? = null
)

fun MalAlternativeTitles?.toAlternativeTitles() = AlternativeTitles(
    synonyms = this?.synonyms.orEmpty(),
    japanese = this?.ja.orEmpty(),
    english = this?.en.orEmpty()
)
