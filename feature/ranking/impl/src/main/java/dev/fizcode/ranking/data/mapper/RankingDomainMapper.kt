package dev.fizcode.ranking.data.mapper

import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.toAlternativeTitles
import dev.fizcode.ranking.domain.model.RankingDomainModel

internal class RankingDomainMapper {

    fun mapToRanking(response: MalListResponse<MalAnimeNode>): RankingDomainModel =
        RankingDomainModel(
            items = response.data.orEmpty().mapIndexedNotNull { index, item ->
                // The API lists the anime in rank order, so the position is the rank when it is missing.
                item.node?.toDomainItem(rank = item.ranking?.rank ?: (index + 1))
            }
        )

    private fun MalAnimeNode.toDomainItem(rank: Int) = RankingDomainModel.Item(
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
