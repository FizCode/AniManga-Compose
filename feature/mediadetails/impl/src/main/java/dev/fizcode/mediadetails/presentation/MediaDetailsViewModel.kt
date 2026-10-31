package dev.fizcode.mediadetails.presentation

import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.domain.usecase.FetchMangaDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkMapper.mapToBookmarkArgs
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkUiMapper
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.mediadetails.presentation.mapper.MangaBookmarkMapper.mapToBookmarkArgs
import dev.fizcode.mediadetails.presentation.mapper.MangaDetailsUiMapper
import dev.fizcode.mediadetails.presentation.model.BookmarkArgument
import dev.fizcode.mediadetails.presentation.model.MediaDetailsEffect
import dev.fizcode.mediadetails.presentation.model.MediaDetailsIntent
import dev.fizcode.mediadetails.presentation.model.MediaDetailsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

internal class MediaDetailsViewModel(
    private val mediaId: Int,
    mediaType: String,
    private val animeDetailsUseCase: FetchAnimeDetailsUseCase,
    private val animeRepository: MediaDetailsRepository,
    private val animeDetailsUiMapper: AnimeDetailsUiMapper,
    private val animeBookmarkUiMapper: AnimeBookmarkUiMapper,
    private val mangaDetailsUseCase: FetchMangaDetailsUseCase,
    private val mangaRepository: MangaDetailsRepository,
    private val mangaDetailsUiMapper: MangaDetailsUiMapper
) : MviViewModel<MediaDetailsUiState, MediaDetailsIntent, MediaDetailsEffect>(MediaDetailsUiState()) {

    private val mediaKind = MediaKind.from(mediaType)

    private val detailsRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val castAndStaffRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val mangaDetailsRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val mangaCharactersRetry = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        animeRepository.isBookmarked(mediaId).collectIntoState { copy(isBookmarked = it) }

        when (mediaKind) {
            MediaKind.ANIME -> loadAnime()
            MediaKind.MANGA -> loadManga()
        }
    }

    override fun onIntent(intent: MediaDetailsIntent) {
        when (intent) {
            MediaDetailsIntent.RetryDetails -> detailsRetry.tryEmit(Unit)
            MediaDetailsIntent.RetryCastAndStaff -> castAndStaffRetry.tryEmit(Unit)
            MediaDetailsIntent.RetryMangaDetails -> mangaDetailsRetry.tryEmit(Unit)
            MediaDetailsIntent.RetryMangaCharacters -> mangaCharactersRetry.tryEmit(Unit)
            MediaDetailsIntent.ToggleBookmark -> toggleBookmark()
        }
    }

    private fun loadAnime() {
        loadSection(
            reloads = detailsRetry,
            mapper = { data -> animeDetailsUiMapper.mapToAnimeDetailsUiModel(domainModel = data) },
            call = { animeDetailsUseCase(mediaId) },
            reduce = { copy(animeDetails = it) }
        )
        loadSection(
            reloads = castAndStaffRetry,
            mapper = animeDetailsUiMapper::mapToAnimeCastUiModel,
            call = { animeRepository.fetchAnimeCast(mediaId) },
            reduce = { copy(animeCast = it) }
        )
        loadSection(
            reloads = castAndStaffRetry,
            mapper = animeDetailsUiMapper::mapToStaffUiModel,
            call = { animeRepository.fetchAnimeStaff(mediaId) },
            reduce = { copy(animeStaff = it) }
        )
    }

    private fun loadManga() {
        loadSection(
            reloads = mangaDetailsRetry,
            mapper = { data -> mangaDetailsUiMapper.mapToMangaDetailsUiModel(domainModel = data) },
            call = { mangaDetailsUseCase(mediaId) },
            reduce = { copy(mangaDetails = it) }
        )
        loadSection(
            reloads = mangaCharactersRetry,
            mapper = mangaDetailsUiMapper::mapToCharacterUiModel,
            call = { mangaRepository.fetchMangaCharacters(mediaId) },
            reduce = { copy(mangaCharacters = it) }
        )
    }

    private fun toggleBookmark() {
        val state = uiState.value
        viewModelScope.launch {
            if (state.isBookmarked) {
                animeRepository.deleteBookmark(mediaId)
                sendEffect(MediaDetailsEffect.Unbookmarked)
            } else {
                val argument = bookmarkArgument(state) ?: return@launch
                animeRepository.bookmarkMedia(
                    bookmarkEntity = animeBookmarkUiMapper.mapToBookmarkDomainModel(argument)
                )
                sendEffect(MediaDetailsEffect.Bookmarked)
            }
        }
    }

    private fun bookmarkArgument(state: MediaDetailsUiState): BookmarkArgument? = when (mediaKind) {
        MediaKind.ANIME -> (state.animeDetails as? UiState.Success)?.data?.mapToBookmarkArgs(mediaId)
        MediaKind.MANGA -> (state.mangaDetails as? UiState.Success)?.data?.mapToBookmarkArgs(mediaId)
    }
}
