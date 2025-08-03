package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.presentation.model.BookmarkArgument

internal class AnimeBookmarkUiMapper {

    fun mapToBookmarkDomainModel(uiModel: BookmarkArgument): BookmarkDomainModel =
        BookmarkDomainModel(
            mediaId = uiModel.mediaId,
            mediaType = uiModel.mediaType,
            posterPath = uiModel.posterPath,
            rating = uiModel.rating,
            title = uiModel.title,
            episodes = uiModel.episodes,
            status = uiModel.status,
            studio = uiModel.studio,
            genres = uiModel.genres
        )
}
