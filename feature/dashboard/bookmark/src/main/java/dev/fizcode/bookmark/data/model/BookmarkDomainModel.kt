package dev.fizcode.bookmark.data.model

internal data class BookmarkDomainModel(
    val mediaId: Int,
    val mediaType: String,
    val posterPath: String,
    val rating: String,
    val title: String,
    val episodes: String,
    val status: String,
    val studio: String,
    val genres: String
)
