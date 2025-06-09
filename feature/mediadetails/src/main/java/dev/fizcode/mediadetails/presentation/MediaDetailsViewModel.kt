package dev.fizcode.mediadetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@HiltViewModel
internal class MediaDetailsViewModel @Inject constructor(
    private val animeDetailsUseCase: FetchAnimeDetailsUseCase,
    private val animeRepository: MediaDetailsRepository,
    private val animeDetailsUiMapper: AnimeDetailsUiMapper
) : ViewModel() {

    private var mediaId: Int = 0

    val animeDetails: StateFlow<UiState<AnimeDetailsUiModel>> = asStateFlow(
        domain = { animeDetailsUseCase(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeDetailsUiModel(data) },
        scope = viewModelScope
    )

    val animeCast: StateFlow<UiState<ImmutableList<AnimeCastUiModel>>> = asStateFlow(
        domain = { animeRepository.fetchAnimeCast(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeCastUiModel(data) },
        scope = viewModelScope
    )

    val animeStaff: StateFlow<UiState<ImmutableList<AnimeStaffUiModel>>> = asStateFlow(
        domain = { animeRepository.fetchAnimeStaff(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToStaffUiModel(data) },
        scope = viewModelScope
    )

    fun fetchMediaId(mediaId: Int) {
        this.mediaId = mediaId
    }
}
