package dev.fizcode.manga.data.mapper

import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.MalMangaNode
import dev.fizcode.mal.response.toAlternativeTitles
import dev.fizcode.manga.domain.model.MangaDomainModel

internal class MangaDomainMapper {

    fun mapToManga(response: MalListResponse<MalMangaNode>): MangaDomainModel = MangaDomainModel(
        items = response.data.orEmpty().mapNotNull { it.node?.toDomainItem() }
    )

    private fun MalMangaNode.toDomainItem() = MangaDomainModel.Item(
        id = id ?: 0,
        title = title.orEmpty(),
        alternativeTitles = alternativeTitles.toAlternativeTitles(),
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
