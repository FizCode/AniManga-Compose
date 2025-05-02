package dev.fizcode.anime.data.mapper

import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.datasource.remote.response.TopRankingResponse

internal class TopRankingDomainMapper {

    fun mapToTopRankingAnime(response: TopRankingResponse): TopRankingDomainModel =
        TopRankingDomainModel(
            data = response.data.toDomainDataModel(),
            paging =response.paging.toDomainPagingModel()
        )

    private fun List<TopRankingResponse.Data>?.toDomainDataModel() = this.orEmpty().map {
        TopRankingDomainModel.Data(node = it.node.toDomainNodeModel())
    }

    private fun TopRankingResponse.Node?.toDomainNodeModel() = this?.run {
        return TopRankingDomainModel.Node(
            id = id.hashCode(),
            title = title.orEmpty(),
            mainPicture = mainPicture.topDomainMainPictureModel(),
            mean = mean ?: 0.0,
            broadcast = broadcast.toDomainBroadcastModel(),
            status = status.orEmpty(),
            mediaType = mediaType.orEmpty(),
            numEpisodes = numEpisodes.hashCode(),
            studios = studios.toDomainStudioModel(),
            genres = genres.toDomainGenreModel()
        )
    } ?: TopRankingDomainModel.Node()

    private fun TopRankingResponse.MainPicture?.topDomainMainPictureModel() = this?.run {
        return TopRankingDomainModel.MainPicture(
            medium = medium.orEmpty(),
            large = large.orEmpty()
        )
    } ?: TopRankingDomainModel.MainPicture()

    private fun TopRankingResponse.Broadcast?.toDomainBroadcastModel() = this?.run {
        return TopRankingDomainModel.Broadcast(
            dayOfTheWeek = dayOfTheWeek.orEmpty(),
            startTime = startTime.orEmpty()
        )
    } ?: TopRankingDomainModel.Broadcast()


    private fun List<TopRankingResponse.Studio>?.toDomainStudioModel() = this.orEmpty().map {
        TopRankingDomainModel.Studio(
            id = it.id.hashCode(),
            name = it.name.orEmpty()
        )
    }

    private fun List<TopRankingResponse.Genre>?.toDomainGenreModel() = this.orEmpty().map {
        TopRankingDomainModel.Genre(
            id = it.id.hashCode(),
            name = it.name.orEmpty()
        )
    }

    private fun TopRankingResponse.Paging?.toDomainPagingModel() =
        TopRankingDomainModel.Paging(
            next = this?.next.orEmpty()
        )

}
