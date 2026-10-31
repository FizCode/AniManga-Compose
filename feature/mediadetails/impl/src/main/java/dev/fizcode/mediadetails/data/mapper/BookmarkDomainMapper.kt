package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.database.bookmark.BookmarkRecord
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel

internal class BookmarkDomainMapper {

    fun mapToBookmarkRecord(data: BookmarkDomainModel): BookmarkRecord =
        BookmarkRecord(
            mediaId = data.mediaId,
            mediaType = data.mediaType,
            posterPath = data.posterPath,
            rating = data.rating,
            title = data.title,
            episodes = data.episodes,
            status = data.status,
            studio = data.studio,
            genres = data.genres,
            alternativeTitles = data.alternativeTitles
        )
}
