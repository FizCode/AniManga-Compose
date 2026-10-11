package dev.fizcode.mediadetails.presentation.model

import dev.fizcode.mediadetails.presentation.header.model.MangaDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaDetailsInfoUiModel

internal data class MangaDetailsUiModel(
    val mangaDetailsHeaderUiModel: MangaDetailsHeaderUiModel,
    val mangaDetailsInfoUiModel: MangaDetailsInfoUiModel,
)
