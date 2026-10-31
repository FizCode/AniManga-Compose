package dev.fizcode.mediadetails.presentation.model

import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import kotlinx.collections.immutable.ImmutableList

internal data class MediaDetailsUiState(
    val isBookmarked: Boolean = false,
    val animeDetails: UiState<AnimeDetailsUiModel> = UiState.Loading,
    val animeCast: UiState<ImmutableList<AnimeCastUiModel>> = UiState.Loading,
    val animeStaff: UiState<ImmutableList<AnimeStaffUiModel>> = UiState.Loading,
    val mangaDetails: UiState<MangaDetailsUiModel> = UiState.Loading,
    val mangaCharacters: UiState<ImmutableList<MangaCharacterUiModel>> = UiState.Loading
)
