package dev.fizcode.mediadetails.presentation.model

internal data class BookmarkArgument(
    val mediaId: Int,
    val mediaType: String,
    val posterPath: String,
    val rating: String,
    val title: String,
    val episodes: String,
    val status: String,
    val studio: String,
    val genres: List<String>
)
