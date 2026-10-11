package dev.fizcode.common.settings

/** How the app is themed: follow the system or force light or dark. */
enum class ThemeMode {
    DEFAULT,
    LIGHT,
    DARK
}

/** Which title of a media is shown. */
enum class TitleStyle {
    DEFAULT,
    SYNONYM,
    JAPANESE,
    ENGLISH;

    /**
     * Picks the title for this style. A style whose title the media does not have, for example a
     * media without a synonym, falls back to the [default] title.
     */
    fun resolve(
        default: String,
        synonyms: List<String> = emptyList(),
        japanese: String? = null,
        english: String? = null
    ): String {
        val picked = when (this) {
            DEFAULT -> default
            SYNONYM -> synonyms.firstOrNull { it.isNotBlank() }
            JAPANESE -> japanese
            ENGLISH -> english
        }
        return picked?.takeIf { it.isNotBlank() } ?: default
    }
}

/** How much adult content media lists may contain. */
enum class NsfwFilter {
    SAFE,
    GREY,
    NOT_SAFE
}
