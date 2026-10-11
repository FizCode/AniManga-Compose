package dev.fizcode.manga.data.mapper

import dev.fizcode.datasource.remote.response.MangaRankingResponse
import dev.fizcode.manga.domain.model.MangaDomainModel

internal class MangaDomainMapper {

    fun mapToManga(response: MangaRankingResponse): MangaDomainModel = MangaDomainModel(
        items = response.data.orEmpty().mapNotNull { it.node?.toDomainItem() }
    )

    private fun MangaRankingResponse.Node.toDomainItem() = MangaDomainModel.Item(
        id = id ?: 0,
        title = title.orEmpty(),
        posterPath = mainPicture?.large.orEmpty(),
        mean = mean ?: 0.0,
        mediaType = mediaType.orEmpty(),
        status = status.orEmpty(),
        numVolumes = numVolumes ?: 0,
        numChapters = numChapters ?: 0,
        synopsis = synopsis.orEmpty(),
        genres = genres.orEmpty().mapNotNull { it.name }
    )
}
