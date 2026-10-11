package dev.fizcode.ranking.data.mapper

import dev.fizcode.datasource.remote.response.TopRankingResponse
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.datasource.remote.response.toAlternativeTitles

internal class RankingDomainMapper {

    fun mapToRanking(response: TopRankingResponse): RankingDomainModel =
        RankingDomainModel(
            items = response.data.orEmpty().mapIndexedNotNull { index, data ->
                // The API lists the anime in rank order, so the position is the rank when it is missing.
                data.node?.toDomainItem(rank = data.ranking?.rank ?: (index + 1))
            }
        )

    private fun TopRankingResponse.Node.toDomainItem(rank: Int) = RankingDomainModel.Item(
        id = id ?: 0,
        rank = rank,
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
