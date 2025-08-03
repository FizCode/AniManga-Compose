package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.common.util.extensions.toStringWithComma
import dev.fizcode.datasource.local.model.BookmarkEntity
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel

internal class BookmarkDomainMapper {

    fun mapTopBookmarkEntity(data: BookmarkDomainModel): BookmarkEntity =
        BookmarkEntity(
            mediaId = data.mediaId,
            mediaType = data.mediaType,
            posterPath = data.posterPath,
            rating = data.rating,
            title = data.title,
            episodes = data.episodes,
            status = data.status,
            studio = data.studio,
            genres = data.genres.toStringWithComma()
        )
}
