package dev.fizcode.bookmark.data.mapper

import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.database.bookmark.BookmarkRecord

internal class BookmarkDomainMapper {

    internal fun mapToBookmarkDomainModel(records: List<BookmarkRecord>) = records.map {
        BookmarkDomainModel(
            mediaId = it.mediaId,
            mediaType = it.mediaType,
            posterPath = it.posterPath,
            rating = it.rating,
            title = it.title,
            episodes = it.episodes,
            status = it.status,
            studio = it.studio,
            genres = it.genres,
            alternativeTitles = it.alternativeTitles
        )
    }

}
