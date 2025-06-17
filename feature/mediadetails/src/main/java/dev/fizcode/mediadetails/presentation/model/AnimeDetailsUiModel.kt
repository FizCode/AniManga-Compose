package dev.fizcode.mediadetails.presentation.model

import dev.fizcode.mediadetailheader.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetailinfo.model.AnimeDetailsInfoUiModel

internal data class AnimeDetailsUiModel(
    val animeDetailsHeaderUiModel: AnimeDetailsHeaderUiModel,
    val animeDetailsInfoUiModel: AnimeDetailsInfoUiModel,
)
