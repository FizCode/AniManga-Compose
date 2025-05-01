package dev.fizcode.anime.presentation.mapper

import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.presentation.model.TopAiringUiModel

internal class TopAiringAnimeUiMapper {

    fun mapToAiringAnimeUiModel(domainModel: TopAiringDomainModel):
            List<TopAiringUiModel> = domainModel.data.map { it.node.toUiData() }

    private fun TopAiringDomainModel.Node.toUiData() = TopAiringUiModel(
        id = id,
        title = title,
        posterPath = mainPicture.large,
        rating = mean.toString()
    )
}
