package dev.fizcode.anime.data.mapper

import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.toAlternativeTitles

internal class TopRankingDomainMapper {

    fun mapToTopRankingAnime(response: MalListResponse<MalAnimeNode>): TopRankingDomainModel =
        TopRankingDomainModel(
            data = response.data.orEmpty().map {
                TopRankingDomainModel.Data(node = it.node.toDomainNodeModel())
            },
            paging = TopRankingDomainModel.Paging(next = response.paging?.next.orEmpty())
        )

    private fun MalAnimeNode?.toDomainNodeModel(): TopRankingDomainModel.Node = this?.let {
        TopRankingDomainModel.Node(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            alternativeTitles = it.alternativeTitles.toAlternativeTitles(),
            mainPicture = TopRankingDomainModel.MainPicture(
                medium = it.mainPicture?.medium.orEmpty(),
                large = it.mainPicture?.large.orEmpty()
            ),
            mean = it.mean ?: 0.0,
            broadcast = TopRankingDomainModel.Broadcast(
                dayOfTheWeek = it.broadcast?.dayOfTheWeek.orEmpty(),
                startTime = it.broadcast?.startTime.orEmpty()
            ),
            status = it.status.orEmpty(),
            mediaType = it.mediaType.orEmpty(),
            numEpisodes = it.numEpisodes ?: 0,
            studios = it.studios.orEmpty().map { studio ->
                TopRankingDomainModel.Studio(id = studio.id ?: 0, name = studio.name.orEmpty())
            },
            genres = it.genres.orEmpty().map { genre ->
                TopRankingDomainModel.Genre(id = genre.id ?: 0, name = genre.name.orEmpty())
            }
        )
    } ?: TopRankingDomainModel.Node()
}
