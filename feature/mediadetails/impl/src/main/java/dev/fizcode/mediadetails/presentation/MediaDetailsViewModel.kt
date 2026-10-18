package dev.fizcode.mediadetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.asUiStateFlow
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchMangaDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.MangaDetailsUiMapper
import dev.fizcode.mediadetails.presentation.model.MangaDetailsUiModel
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
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class MediaDetailsViewModel(
    private val mediaId: Int,
    private val animeDetailsUseCase: FetchAnimeDetailsUseCase,
    private val animeRepository: MediaDetailsRepository,
    private val animeDetailsUiMapper: AnimeDetailsUiMapper,
    private val animeBookmarkUiMapper: AnimeBookmarkUiMapper,
    private val mangaDetailsUseCase: FetchMangaDetailsUseCase,
    private val mangaRepository: MangaDetailsRepository,
    private val mangaDetailsUiMapper: MangaDetailsUiMapper
) : ViewModel() {

    private val detailsRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val castAndStaffRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val mangaDetailsRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val mangaCharactersRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val isBookmarked: StateFlow<Boolean> = animeRepository.isBookmarked(mediaId)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, WhileSubscribed(5_000), false)
    val animeDetails: StateFlow<UiState<AnimeDetailsUiModel>> = asUiStateFlow(
        scope = viewModelScope,
        domain = { animeDetailsUseCase(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeDetailsUiModel(domainModel = data) },
        retrigger = detailsRetry
    )

    val animeCast: StateFlow<UiState<ImmutableList<AnimeCastUiModel>>> = asUiStateFlow(
        scope = viewModelScope,
        domain = { animeRepository.fetchAnimeCast(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToAnimeCastUiModel(data) },
        retrigger = castAndStaffRetry
    )

    val animeStaff: StateFlow<UiState<ImmutableList<AnimeStaffUiModel>>> = asUiStateFlow(
        scope = viewModelScope,
        domain = { animeRepository.fetchAnimeStaff(mediaId) },
        mapper = { data -> animeDetailsUiMapper.mapToStaffUiModel(data) },
        retrigger = castAndStaffRetry
    )

    // Manga flows only start when the manga screen collects them.
    val mangaDetails: StateFlow<UiState<MangaDetailsUiModel>> = asUiStateFlow(
        scope = viewModelScope,
        domain = { mangaDetailsUseCase(mediaId) },
        mapper = { data -> mangaDetailsUiMapper.mapToMangaDetailsUiModel(domainModel = data) },
        retrigger = mangaDetailsRetry
    )

    val mangaCharacters: StateFlow<UiState<ImmutableList<MangaCharacterUiModel>>> = asUiStateFlow(
        scope = viewModelScope,
        domain = { mangaRepository.fetchMangaCharacters(mediaId) },
        mapper = { data -> mangaDetailsUiMapper.mapToCharacterUiModel(data) },
        retrigger = mangaCharactersRetry
    )

    private val _effect = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effect = _effect.asSharedFlow()


    /** Requests the details again, e.g. after a failure. */
    fun retryDetails() {
        detailsRetry.tryEmit(Unit)
    }

    /** Requests the cast and staff again, e.g. after a failure. */
    fun retryCastAndStaff() {
        castAndStaffRetry.tryEmit(Unit)
    }

    /** Requests the manga details again, e.g. after a failure. */
    fun retryMangaDetails() {
        mangaDetailsRetry.tryEmit(Unit)
    }

    /** Requests the manga characters again, e.g. after a failure. */
    fun retryMangaCharacters() {
        mangaCharactersRetry.tryEmit(Unit)
    }

    fun bookmarkMedia(bookmarkData: BookmarkArgument) = viewModelScope.launch {
        if (isBookmarked.value) {
            animeRepository.deleteBookmark(mediaId)
            _effect.emit("Media unbookmarked")
        } else {
            animeRepository.bookmarkMedia(
                bookmarkEntity = animeBookmarkUiMapper.mapToBookmarkDomainModel(bookmarkData)
            )
            _effect.emit("Media bookmarked")
        }
    }
}
