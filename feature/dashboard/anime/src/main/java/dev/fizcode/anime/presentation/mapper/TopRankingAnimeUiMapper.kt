package dev.fizcode.anime.presentation.mapper

import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.presentation.model.TopRankingUiModel
import dev.fizcode.anime.util.Constant
import dev.fizcode.common.util.airingStatus
import dev.fizcode.common.util.animeMediaType

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
            subTitle = "${animeMediaType(mediaType)} | ${
                if (numEpisodes != 0) {
                    "$numEpisodes ${Constant.EPISODES} |"
                } else {
                    ""
                }
            } ${airingStatus(status = status)}",
            studio = studios.joinToString(",") { it.name },
            genre = genres.map { it.name }
        )
}
