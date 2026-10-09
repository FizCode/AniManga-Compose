package dev.fizcode.mediadetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.asStateFlow
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkUiMapper
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import dev.fizcode.mediadetails.presentation.model.BookmarkArgument
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class MediaDetailsViewModel(
    private val animeDetailsUseCase: FetchAnimeDetailsUseCase,
    private val animeRepository: MediaDetailsRepository,
    private val animeDetailsUiMapper: AnimeDetailsUiMapper,
    private val animeBookmarkUiMapper: AnimeBookmarkUiMapper
) : ViewModel() {

    private var mediaId = MutableStateFlow(0)

    val isBookmarked: StateFlow<Boolean> = mediaId
        .flatMapLatest { animeRepository.isBookmarked(it) }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, WhileSubscribed(5_000), false)
    val animeDetails: StateFlow<UiState<AnimeDetailsUiModel>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeDetailsUseCase(mediaId.value) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeDetailsUiModel(domainModel = data) }
    )

    val animeCast: StateFlow<UiState<ImmutableList<AnimeCastUiModel>>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeRepository.fetchAnimeCast(mediaId.value) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeCastUiModel(data) }
    )

    val animeStaff: StateFlow<UiState<ImmutableList<AnimeStaffUiModel>>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeRepository.fetchAnimeStaff(mediaId.value) },
        mapper = { data -> animeDetailsUiMapper.mapToStaffUiModel(data) }
    )

    private val _effect = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effect = _effect.asSharedFlow()


    fun fetchMediaId(mediaId: Int) {
        logMemoryUsage("initiate")
        this.mediaId.value = mediaId
    }

    fun bookmarkMedia(bookmarkData: BookmarkArgument) = viewModelScope.launch {
        logMemoryUsage("Before Bookmark")
        if (isBookmarked.value) {
            animeRepository.deleteBookmark(mediaId.value)
            _effect.emit("Media unbookmarked")
        } else {
            animeRepository.bookmarkMedia(
                bookmarkEntity = animeBookmarkUiMapper.mapToBookmarkDomainModel(bookmarkData)
            )
            _effect.emit("Media bookmarked")
        }
        logMemoryUsage("After Bookmark")
    }

    private fun logMemoryUsage(label: String) {
        // Calling System.gc() is expensive but helps get a more "stable" baseline
        System.gc()
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        println("FizTrace: MemoryComparison [$label]: $usedMemory bytes")
    }
}
