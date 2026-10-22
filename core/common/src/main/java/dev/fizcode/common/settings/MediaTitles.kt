package dev.fizcode.common.settings

/** The other titles a media can have next to its default one. */
data class AlternativeTitles(
    val synonyms: List<String> = emptyList(),
    val japanese: String = "",
    val english: String = ""
)

/** Every title of a media, so the one the user picked in the settings can be shown. */
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
