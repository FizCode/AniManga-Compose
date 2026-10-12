package dev.fizcode.anime.data.mapper

import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.toAlternativeTitles

internal class TopAiringDomainMapper {

    fun mapToTopAiringAnime(response: MalListResponse<MalAnimeNode>): TopAiringDomainModel =
        TopAiringDomainModel(
            data = response.data.orEmpty().map {
                TopAiringDomainModel.Data(node = it.node.toDomainNodeModel())
            },
            paging = TopAiringDomainModel.Paging(next = response.paging?.next.orEmpty()),
            season = TopAiringDomainModel.Season(
                year = response.season?.year ?: 0,
                season = response.season?.season.orEmpty()
            )
        )

    private fun MalAnimeNode?.toDomainNodeModel(): TopAiringDomainModel.Node = this?.let {
        TopAiringDomainModel.Node(
            id = it.id ?: 0,
            mediaType = it.mediaType.orEmpty(),
            title = it.title.orEmpty(),
            alternativeTitles = it.alternativeTitles.toAlternativeTitles(),
            mainPicture = TopAiringDomainModel.MainPicture(
                medium = it.mainPicture?.medium.orEmpty(),
                large = it.mainPicture?.large.orEmpty()
            ),
            mean = it.mean ?: 0.0
        )
    } ?: TopAiringDomainModel.Node()
}
