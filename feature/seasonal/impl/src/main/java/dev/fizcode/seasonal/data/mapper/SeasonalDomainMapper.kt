package dev.fizcode.seasonal.data.mapper

import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.toAlternativeTitles
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel

internal class SeasonalDomainMapper {

    fun mapToSeasonalAnime(response: MalListResponse<MalAnimeNode>): SeasonalDomainModel =
        SeasonalDomainModel(
            items = response.data.orEmpty().mapNotNull { it.node?.toDomainItem() }
        )

    private fun MalAnimeNode.toDomainItem() = SeasonalDomainModel.Item(
        id = id ?: 0,
        mediaType = mediaType.orEmpty(),
        title = title.orEmpty(),
        alternativeTitles = alternativeTitles.toAlternativeTitles(),
        posterPath = mainPicture?.large.orEmpty(),
        mean = mean ?: 0.0,
        broadcastDay = broadcast?.dayOfTheWeek.orEmpty(),
        broadcastTime = broadcast?.startTime.orEmpty(),
        status = status.orEmpty(),
        numEpisodes = numEpisodes ?: 0,
        studios = studios.orEmpty().mapNotNull { it.name },
        synopsis = synopsis.orEmpty(),
        genres = genres.orEmpty().mapNotNull { it.name }
    )
}
