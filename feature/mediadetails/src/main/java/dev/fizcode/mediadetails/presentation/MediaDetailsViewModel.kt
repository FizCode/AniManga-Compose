package dev.fizcode.mediadetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.fizcode.common.base.presentationhandler.asStateFlow
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class MediaDetailsViewModel @Inject constructor(
    private val animeDetailsUseCase: FetchAnimeDetailsUseCase,
    private val animeDetailsUiMapper: AnimeDetailsUiMapper
) : ViewModel() {

    private var mediaId: Int = 0

    val animeDetails: StateFlow<UiState<AnimeDetailsUiModel>> = asStateFlow(
        domain = { animeDetailsUseCase(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeDetailsUiModel(data) },
        scope = viewModelScope
    )

    fun fetchMediaId(mediaId: Int) {
        this.mediaId = mediaId
    }
}
