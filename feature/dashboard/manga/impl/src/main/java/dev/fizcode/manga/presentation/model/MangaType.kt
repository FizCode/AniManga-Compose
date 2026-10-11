package dev.fizcode.manga.presentation.model

import dev.fizcode.manga.util.Constant

/**
 * A type tab. [rankingType] is the MAL `ranking_type` used when sorting by ranking, and [limit]
 * how many entries to ask for. [matches] narrows a list by the item's `media_type`.
 */
internal enum class MangaType(
    val label: String,
    val rankingType: String,
    val limit: Int = Constant.RANKING_LIMIT
) {
    ALL("All", "all"),
    MANGA("Manga", "manga"),
    DOUJINSHI("Doujinshi", "doujin"),
    ONE_SHOT("One-Shot", "oneshots"),

    /** MAL has no single ranking for the remaining types, so everything is fetched and filtered. */
    OTHER("Other", "all", Constant.RANKING_LIMIT_MAX);

    fun matches(mediaType: String): Boolean = when (this) {
        ALL -> true
        MANGA -> mediaType == MEDIA_MANGA
        DOUJINSHI -> mediaType == MEDIA_DOUJINSHI
        ONE_SHOT -> mediaType == MEDIA_ONE_SHOT
        OTHER -> mediaType != MEDIA_MANGA && mediaType != MEDIA_DOUJINSHI && mediaType != MEDIA_ONE_SHOT
    }

    private companion object {
        const val MEDIA_MANGA = "manga"
        const val MEDIA_DOUJINSHI = "doujinshi"
        const val MEDIA_ONE_SHOT = "one_shot"
    }
}
