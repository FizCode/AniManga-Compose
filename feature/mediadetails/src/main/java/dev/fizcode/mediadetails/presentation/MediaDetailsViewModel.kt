package dev.fizcode.mediadetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.presentationhandler.asStateFlow
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.mediadetailinfo.model.AnimeCastUiModel
import dev.fizcode.mediadetailinfo.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

internal class MediaDetailsViewModel(
    private val animeDetailsUseCase: FetchAnimeDetailsUseCase,
    private val animeRepository: MediaDetailsRepository,
    private val animeDetailsUiMapper: AnimeDetailsUiMapper
) : ViewModel() {

    private var mediaId: Int = 0

    val animeDetails: StateFlow<UiState<AnimeDetailsUiModel>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeDetailsUseCase(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeDetailsUiModel(data) }
    )

    val animeCast: StateFlow<UiState<ImmutableList<AnimeCastUiModel>>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeRepository.fetchAnimeCast(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeCastUiModel(data) }
    )

    val animeStaff: StateFlow<UiState<ImmutableList<AnimeStaffUiModel>>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeRepository.fetchAnimeStaff(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToStaffUiModel(data) }
    )

    fun fetchMediaId(mediaId: Int) {
        this.mediaId = mediaId
    }
}
