package dev.fizcode.seasonal.presentation.mapper

import dev.fizcode.common.util.extensions.releaseInfo
import dev.fizcode.seasonal.domain.model.SeasonalDomainModel
import dev.fizcode.seasonal.presentation.model.SeasonalUiModel
import kotlinx.collections.immutable.toImmutableList
import java.util.Locale

internal class SeasonalUiMapper {

    /** The API already sorts by score, so the position in the list is the rank. */
    fun mapToSeasonalUiModel(domainModel: SeasonalDomainModel): List<SeasonalUiModel> =
        domainModel.items.mapIndexed { index, item -> item.toUiModel(rank = index + 1) }

    private fun SeasonalDomainModel.Item.toUiModel(rank: Int) = SeasonalUiModel(
        id = id,
        rank = rank,
        mediaType = mediaType,
        title = title,
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
