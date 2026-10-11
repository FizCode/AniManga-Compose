package dev.fizcode.common.settings

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf

/** The other titles a media can have next to its default one. */
@Immutable
data class AlternativeTitles(
    val synonyms: List<String> = emptyList(),
    val japanese: String = "",
    val english: String = ""
)

/** Every title of a media, so the one the user picked in the settings can be shown. */
@Immutable
data class MediaTitles(
    val default: String,
    val alternative: AlternativeTitles = AlternativeTitles()
) {

    /** The title for [style], the [default] one when the media has no title for that style. */
    fun resolve(style: TitleStyle): String = style.resolve(
        default = default,
        synonyms = alternative.synonyms,
        japanese = alternative.japanese,
        english = alternative.english
    )

    /** True when [query] is inside any of the titles, so a search finds a media by any of them. */
    fun matches(query: String): Boolean = query.isBlank() ||
        (listOf(default, alternative.japanese, alternative.english) + alternative.synonyms)
            .any { it.contains(query.trim(), ignoreCase = true) }
}

/** The title style the user picked in the settings, provided once at the root of the app. */
val LocalTitleStyle = compositionLocalOf { TitleStyle.DEFAULT }
