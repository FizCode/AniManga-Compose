package dev.fizcode.bookmark.presentation.mapper

import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.common.util.extensions.toImmutableList

internal class BookmarkUiMapper {

    internal fun mapToBookmarkUiModel(model: List<BookmarkDomainModel>) = model.map {
        BookmarkUiModel(
            mediaId = it.mediaId,
            mediaType = it.mediaType,
            posterPath = it.posterPath,
            rating = it.rating,
            title = it.title,
            subTitle = subTitle(
                mediaType = it.mediaType,
                episodes = it.episodes,
                status = it.status
            ),
            studio = it.studio,
            genre = it.genres.toImmutableList()
        )
    }

    private fun subTitle(
        mediaType: String,
        episodes: String,
        status: String
    ): String = "$mediaType | $episodes | $status"

}
