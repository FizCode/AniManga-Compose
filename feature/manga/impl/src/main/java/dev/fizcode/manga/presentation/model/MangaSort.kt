package dev.fizcode.manga.presentation.model

/** How the list is ordered. [rankingType] is null when the selected [MangaType] decides it. */
internal enum class MangaSort(val label: String, val rankingType: String?) {
    RANKING("Ranking", null),
    POPULARITY("Popularity", "bypopularity"),
    FAVORITES("Favorites", "favorite")
}
