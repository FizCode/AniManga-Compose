package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.mediadetails.presentation.model.BookmarkArgument
import dev.fizcode.mediadetails.presentation.model.MangaDetailsUiModel

internal object MangaBookmarkMapper {

    /** `episodes` holds the volume and chapter counts, e.g. `Vol. 74 Ch. 705`, and `studio` the magazine. */
    fun MangaDetailsUiModel.mapToBookmarkArgs(mediaId: Int) = BookmarkArgument(
        mediaId = mediaId,
        mediaType = mangaDetailsHeaderUiModel.mediaType,
        posterPath = mangaDetailsHeaderUiModel.posterPath,
        rating = mangaDetailsHeaderUiModel.score,
        title = mangaDetailsHeaderUiModel.title,
        episodes = mangaDetailsHeaderUiModel.publishInfo.substringBefore(" | "),
        status = mangaDetailsInfoUiModel.mangaInfo.status,
        studio = mangaDetailsHeaderUiModel.serialization,
        genres = mangaDetailsHeaderUiModel.genre,
        titles = mangaDetailsHeaderUiModel.titles
    )
}
