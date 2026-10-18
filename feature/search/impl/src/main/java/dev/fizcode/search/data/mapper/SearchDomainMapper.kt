package dev.fizcode.search.data.mapper

import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.toAlternativeTitles
import dev.fizcode.search.domain.model.SearchAnimeDomainModel

internal class SearchDomainMapper {

    fun mapToSearchAnime(response: MalListResponse<MalAnimeNode>): SearchAnimeDomainModel =
        SearchAnimeDomainModel(
            data = response.data.orEmpty().mapNotNull { it.node?.toDomainNodeModel() }
        )

    private fun MalAnimeNode.toDomainNodeModel() =
        SearchAnimeDomainModel.Node(
            id = id ?: 0,
            mediaType = mediaType.orEmpty(),
            title = title.orEmpty(),
            alternativeTitles = alternativeTitles.toAlternativeTitles(),
            posterPath = mainPicture?.large.orEmpty(),
            mean = mean ?: 0.0,
            status = status.orEmpty(),
            numEpisodes = numEpisodes ?: 0,
            genres = genres.orEmpty().mapNotNull { it.name }
        )
}
