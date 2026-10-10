package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import dev.fizcode.mediadetails.presentation.model.BookmarkArgument

internal object AnimeBookmarkMapper {

    fun AnimeDetailsUiModel.mapToBookmarkArgs(mediaId: Int) = BookmarkArgument(
        mediaId = mediaId,
        mediaType = this.animeDetailsHeaderUiModel.mediaType,
        posterPath = this.animeDetailsHeaderUiModel.posterPath,
        rating = this.animeDetailsHeaderUiModel.score,
        title = this.animeDetailsHeaderUiModel.title,
        episodes = this.animeDetailsInfoUiModel.animeInfo.episodes,
        status = this.animeDetailsInfoUiModel.animeInfo.status,
        studio = this.animeDetailsHeaderUiModel.studio,
        genres = this.animeDetailsHeaderUiModel.genre
    )

}
