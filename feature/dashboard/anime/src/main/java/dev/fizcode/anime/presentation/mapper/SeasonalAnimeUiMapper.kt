package dev.fizcode.anime.presentation.mapper

import dev.fizcode.anime.domain.model.Node
import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.anime.util.Constant
import dev.fizcode.common.util.capitalizeFirstChar

internal class SeasonalAnimeUiMapper {

    fun mapToSeasonalAnimeUiModel(domainModel: SeasonalAnimeDomainModel):
            List<SeasonalUiModel> = domainModel.data.map { it.node.toUiData() }

    private fun Node.toUiData(): SeasonalUiModel =
        SeasonalUiModel(
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

private fun releaseInfo(
    broadcastDay: String,
    broadcastTime: String,
    status: String,
    numEpisodes: Int
): String {

    val formatedNumEps = "${
        if (numEpisodes != 0) "$numEpisodes ${Constant.EPISODES} | " else ""
    }${Constant.FINISHED_AIRING}"

    val formatedBroadcastDay = if (broadcastDay.isNotEmpty()) {
        "${capitalizeFirstChar(broadcastDay)}, $broadcastTime (${Constant.JST})"
    } else {
        Constant.UNKNOWN_BROADCAST_TIME
    }

    return when (status) {
        Constant.STATUS_FINISHED_AIRING -> formatedNumEps

        Constant.STATUS_CURRENTLY_AIRING -> formatedBroadcastDay

        Constant.STATUS_NOT_YET_AIRED -> Constant.NOT_YET_AIRED

        else -> Constant.UNKNOWN_AIRING_STATUS
    }
}
