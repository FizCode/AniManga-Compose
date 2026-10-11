package dev.fizcode.anime.presentation.mapper

import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.presentation.model.TopAiringUiModel
import dev.fizcode.common.settings.MediaTitles

internal class TopAiringAnimeUiMapper {

    fun mapToAiringAnimeUiModel(domainModel: TopAiringDomainModel):
            List<TopAiringUiModel> = domainModel.data.map { it.node.toUiData() }

    private fun TopAiringDomainModel.Node.toUiData() = TopAiringUiModel(
        id = id,
        mediaType = mediaType,
        title = title,
        titles = MediaTitles(title, alternativeTitles),
        posterPath = mainPicture.large,
        rating = mean.toString()
    )
}
