package dev.fizcode.anime.data.mapper

import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.datasource.remote.response.TopAiringAnimeResponse

internal class TopAiringDomainMapper {

    fun mapToTopAiringAnime(response: TopAiringAnimeResponse): TopAiringDomainModel =
        TopAiringDomainModel(
            data = response.data.toDomainDataModel(),
            paging = response.paging.toDomainPagingModel(),
            season = response.season.toDomainSeasonModel()
        )

    private fun List<TopAiringAnimeResponse.Data>?.toDomainDataModel() = this.orEmpty().map {
        TopAiringDomainModel.Data(node = it.node.toDomainNodeModel())
    }

    private fun TopAiringAnimeResponse.Node?.toDomainNodeModel() = this?.run {
        return TopAiringDomainModel.Node(
            id = id.hashCode(),
            title = title.orEmpty(),
            mainPicture = mainPicture.toDomainMainPictureModel(),
            mean = mean ?: 0.0
        )
    } ?: TopAiringDomainModel.Node()

    private fun TopAiringAnimeResponse.MainPicture?.toDomainMainPictureModel() = this?.run {
        return TopAiringDomainModel.MainPicture(
            medium = medium.orEmpty(),
            large = large.orEmpty()
        )
    } ?: TopAiringDomainModel.MainPicture()

    private fun TopAiringAnimeResponse.Paging?.toDomainPagingModel() =
        TopAiringDomainModel.Paging(
            next = this?.next.orEmpty()
        )

    private fun TopAiringAnimeResponse.Season?.toDomainSeasonModel() = this?.run {
        return TopAiringDomainModel.Season(
            year = year.hashCode(),
            season = season.orEmpty()
        )
    } ?: TopAiringDomainModel.Season()
}
