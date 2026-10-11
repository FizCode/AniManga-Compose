package dev.fizcode.bookmark.presentation.model

/**
 * A type tab on the bookmark screen. [matches] narrows the list by the media type label stored
 * with the bookmark (e.g. `TV`, `Manga`, `One-Shot`).
 */
internal enum class BookmarkTab(val label: String, val emptyLabel: String) {
    ANIME("Anime", "anime"),
    MANGA("Manga", "manga"),
    DOUJINSHI("Doujinshi", "doujinshi"),
    ONE_SHOT("One-Shot", "one-shot"),

    /** Novels, manhwa, manhua and the other manga-like types. */
    OTHER("Other", "other");

    fun matches(mediaType: String): Boolean {
        val type = normalize(mediaType)
        return when (this) {
            ANIME -> type !in MANGA_LIKE_TYPES
            MANGA -> type == TYPE_MANGA
            DOUJINSHI -> type == TYPE_DOUJINSHI
            ONE_SHOT -> type == TYPE_ONE_SHOT
            OTHER -> type in MANGA_LIKE_TYPES &&
                type != TYPE_MANGA && type != TYPE_DOUJINSHI && type != TYPE_ONE_SHOT
        }
    }

    private companion object {
        const val TYPE_MANGA = "manga"
        const val TYPE_DOUJINSHI = "doujinshi"
        const val TYPE_ONE_SHOT = "one_shot"
        val MANGA_LIKE_TYPES = setOf(
            TYPE_MANGA, TYPE_DOUJINSHI, TYPE_ONE_SHOT,
            "novel", "light_novel", "manhwa", "manhua", "oel"
        )

        fun normalize(mediaType: String) =
            mediaType.trim().lowercase().replace('-', '_').replace(' ', '_')
    }
}
