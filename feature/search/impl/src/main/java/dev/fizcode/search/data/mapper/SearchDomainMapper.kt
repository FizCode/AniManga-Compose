package dev.fizcode.search.data.mapper

import dev.fizcode.datasource.remote.response.SearchAnimeResponse
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.datasource.remote.response.toAlternativeTitles

internal class SearchDomainMapper {

    fun mapToSearchAnime(response: SearchAnimeResponse): SearchAnimeDomainModel =
        SearchAnimeDomainModel(
            data = response.data.orEmpty().mapNotNull { it.node?.toDomainNodeModel() }
        )

    private fun SearchAnimeResponse.Node.toDomainNodeModel() =
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
