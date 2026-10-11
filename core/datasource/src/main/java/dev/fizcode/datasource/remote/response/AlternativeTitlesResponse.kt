package dev.fizcode.datasource.remote.response

import dev.fizcode.common.settings.AlternativeTitles
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The `alternative_titles` field of a MyAnimeList media, only sent when it is asked for. */
@Serializable
data class AlternativeTitlesResponse(
    @SerialName("synonyms")
    val synonyms: List<String>? = null,
    @SerialName("en")
    val en: String? = null,
    @SerialName("ja")
    val ja: String? = null
)

fun AlternativeTitlesResponse?.toAlternativeTitles() = AlternativeTitles(
    synonyms = this?.synonyms.orEmpty(),
    japanese = this?.ja.orEmpty(),
    english = this?.en.orEmpty()
)
