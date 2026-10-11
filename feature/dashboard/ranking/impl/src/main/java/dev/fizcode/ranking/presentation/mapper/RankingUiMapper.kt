package dev.fizcode.ranking.presentation.mapper

import dev.fizcode.common.util.extensions.releaseInfo
import dev.fizcode.ranking.domain.model.RankingDomainModel
import dev.fizcode.ranking.presentation.model.RankingUiModel
import kotlinx.collections.immutable.toImmutableList
import java.util.Locale
import dev.fizcode.common.settings.MediaTitles

internal class RankingUiMapper {

    fun mapToRankingUiModel(domainModel: RankingDomainModel): List<RankingUiModel> =
        domainModel.items.map { it.toUiModel() }

    private fun RankingDomainModel.Item.toUiModel() = RankingUiModel(
        id = id,
        rank = rank,
        mediaType = mediaType,
        title = title,
        titles = MediaTitles(title, alternativeTitles),
        posterPath = posterPath,
        rating = String.format(Locale.US, "%.2f", mean),
        releaseInfo = releaseInfo(
            broadcastDay = broadcastDay,
            broadcastTime = broadcastTime,
            status = status,
            numEpisodes = numEpisodes
        ),
        studio = studios.joinToString(", "),
        synopsis = synopsis,
        genre = genres.toImmutableList()
    )
}
