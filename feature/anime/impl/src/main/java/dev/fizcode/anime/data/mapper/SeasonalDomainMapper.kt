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
import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.toAlternativeTitles

internal class SeasonalDomainMapper {

    fun mapToSeasonAnime(response: MalListResponse<MalAnimeNode>): SeasonalAnimeDomainModel =
        SeasonalAnimeDomainModel(
            data = response.data.orEmpty().map { Data(node = it.node.toDomainNodeModel()) },
            paging = Paging(next = response.paging?.next.orEmpty()),
            season = Season(
                year = response.season?.year ?: 0,
                season = response.season?.season.orEmpty()
            )
        )

    private fun MalAnimeNode?.toDomainNodeModel(): Node = this?.let {
        Node(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            alternativeTitles = it.alternativeTitles.toAlternativeTitles(),
            mainPicture = MainPicture(
                medium = it.mainPicture?.medium.orEmpty(),
                large = it.mainPicture?.large.orEmpty()
            ),
            mean = it.mean ?: 0.0,
            broadcast = Broadcast(
                dayOfTheWeek = it.broadcast?.dayOfTheWeek.orEmpty(),
                startTime = it.broadcast?.startTime.orEmpty()
            ),
            status = it.status.orEmpty(),
            mediaType = it.mediaType.orEmpty(),
            numEpisodes = it.numEpisodes ?: 0,
            studios = it.studios.orEmpty().map { studio ->
                Studio(id = studio.id ?: 0, name = studio.name.orEmpty())
            },
            synopsis = it.synopsis.orEmpty(),
            genres = it.genres.orEmpty().map { genre ->
                Genre(id = genre.id ?: 0, name = genre.name.orEmpty())
            }
        )
    } ?: Node()
}
