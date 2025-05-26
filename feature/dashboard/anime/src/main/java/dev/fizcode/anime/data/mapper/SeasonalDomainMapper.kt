package dev.fizcode.anime.data.mapper

import dev.fizcode.anime.domain.model.Broadcast
import dev.fizcode.anime.domain.model.Data
import dev.fizcode.anime.domain.model.Genre
import dev.fizcode.anime.domain.model.MainPicture
import dev.fizcode.anime.domain.model.Node
import dev.fizcode.anime.domain.model.Paging
import dev.fizcode.anime.domain.model.Season
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.Studio
import dev.fizcode.datasource.remote.response.CurrentSeasonAnimeResponse

internal class SeasonalDomainMapper {

    fun mapToSeasonAnime(response: CurrentSeasonAnimeResponse): SeasonalAnimeDomainModel =
        SeasonalAnimeDomainModel(
            data = response.data.toDomainDataModel(),
            paging = response.paging.toDomainPagingModel(),
            season = response.season.toDomainSeasonModel()
        )

    private fun List<CurrentSeasonAnimeResponse.Data>?.toDomainDataModel() = this.orEmpty().map {
        Data(node = it.node.toDomainNodeModel())
    }

    private fun CurrentSeasonAnimeResponse.Node?.toDomainNodeModel() = this?.run {
        return Node(
            id = id ?: 0,
            title = title.orEmpty(),
            mainPicture = mainPicture.toDomainMainPictureModel(),
            mean = mean ?: 0.0,
            broadcast = broadcast.toDomainBroadcastModel(),
            status = status.orEmpty(),
            mediaType = mediaType.orEmpty(),
            numEpisodes = numEpisodes ?: 0,
            studios = studios.toDomainStudioModel(),
            synopsis = synopsis.orEmpty(),
            genres = genres.toDomainGenreModel()
        )
    } ?: Node()

    private fun CurrentSeasonAnimeResponse.MainPicture?.toDomainMainPictureModel(): MainPicture =
        this?.run {
            return MainPicture(
                medium = medium.orEmpty(),
                large = large.orEmpty()
            )
        } ?: MainPicture()

    private fun CurrentSeasonAnimeResponse.Broadcast?.toDomainBroadcastModel(): Broadcast =
        this?.run {
            return Broadcast(
                dayOfTheWeek = dayOfTheWeek.orEmpty(),
                startTime = startTime.orEmpty()
            )
        } ?: Broadcast()

    private fun List<CurrentSeasonAnimeResponse.Studio>?.toDomainStudioModel() = this.orEmpty().map {
        Studio(
            id = it.id ?: 0,
            name = it.name.orEmpty()
        )
    }

    private fun List<CurrentSeasonAnimeResponse.Genre>?.toDomainGenreModel() = this.orEmpty().map {
        Genre(
            id = it.id ?: 0,
            name = it.name.orEmpty()
        )
    }

    private fun CurrentSeasonAnimeResponse.Paging?.toDomainPagingModel(): Paging = this?.run {
        return Paging(
            next = next.orEmpty()
        )
    } ?: Paging()

    private fun CurrentSeasonAnimeResponse.Season?.toDomainSeasonModel(): Season = this?.run {
        return Season(
            year = year ?: 0,
            season = season.orEmpty()
        )
    } ?: Season()
}
