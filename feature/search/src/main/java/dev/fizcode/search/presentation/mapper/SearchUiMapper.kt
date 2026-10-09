package dev.fizcode.search.presentation.mapper

import dev.fizcode.common.util.extensions.airingStatus
import dev.fizcode.common.util.extensions.animeMediaType
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.presentation.model.SearchResultUiModel
import dev.fizcode.search.util.Constant
import kotlinx.collections.immutable.toImmutableList
import java.util.Locale

internal class SearchUiMapper {

    fun mapToSearchResultUiModel(domainModel: SearchAnimeDomainModel): List<SearchResultUiModel> =
        domainModel.data.map { it.toUiData() }

    private fun SearchAnimeDomainModel.Node.toUiData() =
        SearchResultUiModel(
            id = id,
            mediaType = mediaType,
            posterPath = posterPath,
            rating = String.format(Locale.US, "%.2f", mean),
            title = title,
            subTitle = subTitle(mediaType, numEpisodes, status),
            genre = genres.toImmutableList()
        )

    private fun subTitle(
        mediaType: String,
        numEpisodes: Int,
        status: String
    ): String {
        val animeMediaType = animeMediaType(mediaType = mediaType)
        val airingStatus = airingStatus(status = status)
        val episode = when (numEpisodes) {
            0 -> ""
            1 -> "$numEpisodes ${Constant.EPISODE} |"
            else -> "$numEpisodes ${Constant.EPISODES} |"
        }

        return "$animeMediaType | $episode $airingStatus"
    }
}
