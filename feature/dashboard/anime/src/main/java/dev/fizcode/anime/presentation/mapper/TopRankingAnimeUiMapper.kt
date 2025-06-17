package dev.fizcode.anime.presentation.mapper

import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.presentation.model.TopRankingUiModel
import dev.fizcode.anime.util.Constant
import dev.fizcode.common.util.extensions.airingStatus
import dev.fizcode.common.util.extensions.animeMediaType
import kotlinx.collections.immutable.toImmutableList

internal class TopRankingAnimeUiMapper {

    fun mapToTopRankingAnimeUiModel(domainModel: TopRankingDomainModel):
            List<TopRankingUiModel> = domainModel.data.map { it.node.toUiData() }

    private fun TopRankingDomainModel.Node.toUiData() =
        TopRankingUiModel(
            id = id,
            mediaType = mediaType,
            posterPath = mainPicture.large,
            rating = mean.toString(),
            title = title,
            subTitle = subTitle(mediaType, numEpisodes, status),
            studio = studios.joinToString(", ") { it.name },
            genre = genres.map { it.name }.toImmutableList()
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
