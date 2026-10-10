package dev.fizcode.mediadetails.domain.model

internal data class BookmarkDomainModel(
    val mediaId: Int = 0,
    val mediaType: String = "",
    val posterPath: String = "",
    val rating: String = "",
    val title: String = "",
    val episodes: String = "",
    val status: String = "",
    val studio: String = "",
    val genres: List<String> = emptyList()
)
