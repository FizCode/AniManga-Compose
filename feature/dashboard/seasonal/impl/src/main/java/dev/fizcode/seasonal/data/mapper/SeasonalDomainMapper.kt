package dev.fizcode.seasonal.data.mapper

import dev.fizcode.datasource.remote.response.CurrentSeasonAnimeResponse
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel

internal class SeasonalDomainMapper {

    fun mapToSeasonalAnime(response: CurrentSeasonAnimeResponse): SeasonalDomainModel =
        SeasonalDomainModel(
            items = response.data.orEmpty().mapNotNull { it.node?.toDomainItem() }
        )

    private fun CurrentSeasonAnimeResponse.Node.toDomainItem() = SeasonalDomainModel.Item(
        id = id ?: 0,
        mediaType = mediaType.orEmpty(),
        title = title.orEmpty(),
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
