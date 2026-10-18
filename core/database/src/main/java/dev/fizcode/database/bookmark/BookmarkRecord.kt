package dev.fizcode.database.bookmark

import dev.fizcode.common.settings.AlternativeTitles

/** A saved bookmark as the features see it, without anything about how it is stored. */
data class BookmarkRecord(
    val mediaId: Int,
    val mediaType: String,
    val posterPath: String,
    val rating: String,
    val title: String,
    val episodes: String,
    val status: String,
    val studio: String,
    val genres: String,
    val alternativeTitles: AlternativeTitles = AlternativeTitles()
)
