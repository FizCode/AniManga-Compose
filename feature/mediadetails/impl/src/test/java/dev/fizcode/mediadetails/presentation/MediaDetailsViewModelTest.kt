package dev.fizcode.mediadetails.presentation

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mediadetails.MainDispatcherRule
import dev.fizcode.mediadetails.awaitValue
import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.Broadcast
import dev.fizcode.mediadetails.domain.model.MainPicture
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.StartSeason
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.StaffDomainModel
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchMangaDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.MangaDetailsUiMapper
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkUiMapper
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.mediadetails.presentation.model.MediaDetailsEffect
import dev.fizcode.mediadetails.presentation.model.MediaDetailsIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import dev.fizcode.mediadetails.domain.model.PagedResult

class MediaDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeMediaDetailsRepository : MediaDetailsRepository {
        val bookmarked = MutableStateFlow(false)
        val requestedCastIds = mutableListOf<Int>()
        val saved = mutableListOf<BookmarkDomainModel>()
        val deleted = mutableListOf<Int>()

        var detailsResult: DomainNetworkState<AnimeDetailsDomainModel> = DomainNetworkState.Empty
        var castResult: DomainNetworkState<List<CastDomainModel>> = DomainNetworkState.Empty
        var staffResult: DomainNetworkState<List<StaffDomainModel>> = DomainNetworkState.Empty

        var detailsCalls = 0
        var castCalls = 0

        override suspend fun fetchAnimeDetails(animeId: Int): DomainNetworkState<AnimeDetailsDomainModel> {
            detailsCalls++
            return detailsResult
        }

        override suspend fun fetchAnimeCast(animeId: Int): DomainNetworkState<List<CastDomainModel>> {
            requestedCastIds += animeId
            castCalls++
            return castResult
        }

        override suspend fun fetchAnimeStaff(animeId: Int) = staffResult

        override suspend fun fetchAnimeCastPage(animeId: Int, page: Int, perPage: Int) =
            DomainNetworkState.Empty as DomainNetworkState<PagedResult<CastDomainModel>>

        override suspend fun fetchAnimeStaffPage(animeId: Int, page: Int, perPage: Int) =
            DomainNetworkState.Empty as DomainNetworkState<PagedResult<StaffDomainModel>>

        override fun isBookmarked(animeId: Int): Flow<Boolean> = bookmarked

        override suspend fun bookmarkMedia(bookmarkEntity: BookmarkDomainModel): Long {
            saved += bookmarkEntity
            bookmarked.value = true
            return 1L
        }

        override suspend fun deleteBookmark(mediaId: Int): Int {
            deleted += mediaId
            bookmarked.value = false
            return 1
        }
    }

    private class FakeMangaDetailsRepository : MangaDetailsRepository {
        var detailsResult: DomainNetworkState<MangaDetailsDomainModel> = DomainNetworkState.Empty
        var charactersResult: DomainNetworkState<List<CastDomainModel>> = DomainNetworkState.Empty
        val requestedMangaIds = mutableListOf<Int>()

        override suspend fun fetchMangaDetails(mangaId: Int): DomainNetworkState<MangaDetailsDomainModel> {
            requestedMangaIds += mangaId
            return detailsResult
        }

        override suspend fun fetchMangaCharacters(mangaId: Int) = charactersResult

        override suspend fun fetchMangaCharactersPage(mangaId: Int, page: Int, perPage: Int) =
            DomainNetworkState.Empty as DomainNetworkState<PagedResult<CastDomainModel>>
    }

    private val repository = FakeMediaDetailsRepository()
    private val mangaRepository = FakeMangaDetailsRepository()

    private fun viewModel(mediaId: Int = 7, mediaType: String = "tv") = MediaDetailsViewModel(
        mediaId = mediaId,
        mediaType = mediaType,
        animeDetailsUseCase = FetchAnimeDetailsUseCase(repository),
        animeRepository = repository,
        animeDetailsUiMapper = AnimeDetailsUiMapper(),
        animeBookmarkUiMapper = AnimeBookmarkUiMapper(),
        mangaDetailsUseCase = FetchMangaDetailsUseCase(mangaRepository),
        mangaRepository = mangaRepository,
        mangaDetailsUiMapper = MangaDetailsUiMapper()
    )

    private val animeDetails = AnimeDetailsDomainModel(
        malDomainModel = MalAnimeDetailsDomainModel(
            alternativeTitles = AlternativeTitles(),
            broadcast = Broadcast(),
            mainPicture = MainPicture(),
            startSeason = StartSeason(),
            title = "Bleach",
            mediaType = "tv"
        )
    )

    @Test
    fun `cast is fetched with the media id and mapped`() = runBlocking<Unit> {
        repository.castResult = DomainNetworkState.Success(
            listOf(CastDomainModel(role = "Main", character = CastDomainModel.Character(name = "Ichigo")))
        )
        val vm = viewModel()

        val state = vm.uiState.awaitValue { it.animeCast is UiState.Success }

        assertEquals("Ichigo", (state.animeCast as UiState.Success).data.single().character)
        assertTrue(7 in repository.requestedCastIds)
    }

    @Test
    fun `staff failure becomes an error state`() = runBlocking<Unit> {
        repository.staffResult = DomainNetworkState.ErrorNetwork(
            exception = IllegalStateException("boom"),
            message = "boom"
        )
        val vm = viewModel()

        val state = vm.uiState.awaitValue { it.animeStaff is UiState.Error }

        assertEquals("boom", (state.animeStaff as UiState.Error).message)
    }

    @Test
    fun `details failure becomes an error state`() = runBlocking<Unit> {
        repository.detailsResult = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()

        vm.uiState.awaitValue { it.animeDetails is UiState.Error }
    }

    @Test
    fun `an anime only loads the anime sections`() = runBlocking<Unit> {
        val vm = viewModel(mediaType = "tv")

        vm.uiState.awaitValue { it.animeDetails !is UiState.Loading }

        assertEquals(1, repository.detailsCalls)
        assertTrue(mangaRepository.requestedMangaIds.isEmpty())
    }

    @Test
    fun `a manga only loads the manga sections`() = runBlocking<Unit> {
        val vm = viewModel(mediaType = "manga")

        vm.uiState.awaitValue { it.mangaDetails !is UiState.Loading }

        assertEquals(listOf(7), mangaRepository.requestedMangaIds)
        assertEquals(0, repository.detailsCalls)
        assertEquals(0, repository.castCalls)
    }

    @Test
    fun `bookmark state follows the repository`() = runBlocking<Unit> {
        val vm = viewModel()
        assertFalse(vm.uiState.value.isBookmarked)

        repository.bookmarked.value = true

        assertTrue(vm.uiState.awaitValue { it.isBookmarked }.isBookmarked)
    }

    @Test
    fun `bookmarking an unbookmarked media saves it and tells the screen`() = runBlocking<Unit> {
        repository.detailsResult = DomainNetworkState.Success(animeDetails)
        val vm = viewModel()
        vm.uiState.awaitValue { it.animeDetails is UiState.Success }

        vm.onIntent(MediaDetailsIntent.ToggleBookmark)

        assertEquals(MediaDetailsEffect.Bookmarked, withTimeout(5_000) { vm.effect.first() })
        assertEquals(listOf(7), repository.saved.map { it.mediaId })
        assertEquals("Bleach", repository.saved.single().title)
        assertTrue(repository.deleted.isEmpty())
    }

    @Test
    fun `bookmarking a bookmarked media removes it and tells the screen`() = runBlocking<Unit> {
        repository.bookmarked.value = true
        val vm = viewModel()
        vm.uiState.awaitValue { it.isBookmarked }

        vm.onIntent(MediaDetailsIntent.ToggleBookmark)

        assertEquals(MediaDetailsEffect.Unbookmarked, withTimeout(5_000) { vm.effect.first() })
        assertEquals(listOf(7), repository.deleted)
        assertTrue(repository.saved.isEmpty())
    }

    @Test
    fun `nothing is bookmarked before the details are loaded`() = runBlocking<Unit> {
        repository.detailsResult = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        vm.uiState.awaitValue { it.animeDetails is UiState.Error }

        vm.onIntent(MediaDetailsIntent.ToggleBookmark)

        assertTrue(repository.saved.isEmpty())
    }

    @Test
    fun `retrying details fetches them again`() = runBlocking<Unit> {
        repository.detailsResult = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        vm.uiState.awaitValue { it.animeDetails is UiState.Error }
        assertEquals(1, repository.detailsCalls)

        vm.onIntent(MediaDetailsIntent.RetryDetails)

        assertEquals(2, repository.detailsCalls)
    }

    @Test
    fun `retrying cast and staff does not refetch details`() = runBlocking<Unit> {
        repository.detailsResult = DomainNetworkState.ErrorNetwork(message = "offline")
        repository.castResult = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        vm.uiState.awaitValue { it.animeCast is UiState.Error && it.animeDetails is UiState.Error }

        vm.onIntent(MediaDetailsIntent.RetryCastAndStaff)

        assertEquals(2, repository.castCalls)
        assertEquals(1, repository.detailsCalls)
    }
}
