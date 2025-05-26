package dev.fizcode.anime.presentation.mapper

import dev.fizcode.anime.domain.model.Node
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.common.util.releaseInfo

internal class SeasonalAnimeUiMapper {

    fun mapToSeasonalAnimeUiModel(domainModel: SeasonalAnimeDomainModel):
            List<SeasonalUiModel> = domainModel.data.map { it.node.toUiData() }

    private fun Node.toUiData(): SeasonalUiModel {
        return SeasonalUiModel(
            id = id,
            mediaType = mediaType,
            title = title,
            posterPath = mainPicture.large,
            releaseInfo = releaseInfo(
                broadcastDay = broadcast.dayOfTheWeek,
                broadcastTime = broadcast.startTime,
                status = status,
                numEpisodes = numEpisodes
            ),
            studio = studios.map { it.name },
            synopsis = synopsis,
            rating = mean.toString(),
            genre = genres.map { it.name }
        )
    }
}
