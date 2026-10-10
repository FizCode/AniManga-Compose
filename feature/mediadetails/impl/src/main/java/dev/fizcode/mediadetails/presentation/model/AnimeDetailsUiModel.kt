package dev.fizcode.mediadetails.presentation.model

import dev.fizcode.mediadetails.presentation.header.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetailsInfoUiModel

internal data class AnimeDetailsUiModel(
    val animeDetailsHeaderUiModel: AnimeDetailsHeaderUiModel,
    val animeDetailsInfoUiModel: AnimeDetailsInfoUiModel,
)
