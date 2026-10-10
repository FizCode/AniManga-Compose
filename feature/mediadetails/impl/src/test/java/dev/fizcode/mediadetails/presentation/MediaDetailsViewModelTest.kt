package dev.fizcode.mediadetails.presentation

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mediadetails.MainDispatcherRule
import dev.fizcode.mediadetails.awaitValue
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.BookmarkDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkUiMapper
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.mediadetails.presentation.model.BookmarkArgument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MediaDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeMediaDetailsRepository : MediaDetailsRepository {
        val bookmarked = MutableStateFlow(false)
        val requestedCastIds = mutableListOf<Int>()
        val saved = mutableListOf<BookmarkDomainModel>()
        val deleted = mutableListOf<Int>()

        var detailsResult: DomainNetworkState<AnimeDetailsDomainModel> = DomainNetworkState.Empty
        var castResult: DomainNetworkState<List<JikanCastDomainModel>> = DomainNetworkState.Empty
        var staffResult: DomainNetworkState<List<JikanStaffDomainModel>> = DomainNetworkState.Empty

        var detailsCalls = 0
        var castCalls = 0

        override suspend fun fetchAnimeDetails(animeId: Int, fields: String): DomainNetworkState<AnimeDetailsDomainModel> {
            detailsCalls++
            return detailsResult
        }

        override suspend fun fetchAnimeCast(animeId: Int): DomainNetworkState<List<JikanCastDomainModel>> {
            requestedCastIds += animeId
            castCalls++
            return castResult
        }

        override suspend fun fetchAnimeStaff(animeId: Int) = staffResult

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

    private val repository = FakeMediaDetailsRepository()

    private fun viewModel() = MediaDetailsViewModel(
        animeDetailsUseCase = FetchAnimeDetailsUseCase(repository),
        animeRepository = repository,
        animeDetailsUiMapper = AnimeDetailsUiMapper(),
        animeBookmarkUiMapper = AnimeBookmarkUiMapper()
    )

    private val argument = BookmarkArgument(
        mediaId = 7,
        mediaType = "TV",
        posterPath = "poster",
        rating = "8.0",
        title = "Bleach",
        episodes = "12 Episodes",
        status = "Finished",
        studio = "Pierrot",
        genres = listOf("Action")
    )

    @Test
    fun `cast is fetched with the media id and mapped`() = runBlocking {
        repository.castResult = DomainNetworkState.Success(
            listOf(JikanCastDomainModel(role = "Main", character = JikanCastDomainModel.Character(name = "Ichigo")))
        )
        val vm = viewModel()
        vm.fetchMediaId(7)
        val job = vm.animeCast.onEach { }.launchIn(this)

        val state = vm.animeCast.awaitValue { it is UiState.Success }

        assertEquals("Ichigo", (state as UiState.Success).data.single().character)
        assertTrue(7 in repository.requestedCastIds)
        job.cancel()
    }

    @Test
    fun `staff failure becomes an error state`() = runBlocking {
        repository.staffResult = DomainNetworkState.ErrorNetwork(
            exception = IllegalStateException("boom"),
            message = "boom"
        )
        val vm = viewModel()
        val job = vm.animeStaff.onEach { }.launchIn(this)

        val state = vm.animeStaff.awaitValue { it is UiState.ErrorException }

        assertEquals("boom", (state as UiState.ErrorException).message)
        job.cancel()
    }

    @Test
    fun `details failure becomes an error state`() = runBlocking {
        repository.detailsResult = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        val job = vm.animeDetails.onEach { }.launchIn(this)

        vm.animeDetails.awaitValue { it is UiState.ErrorException }
        job.cancel()
    }

    @Test
    fun `bookmark state follows the repository`() = runBlocking {
        val vm = viewModel()
        val job = vm.isBookmarked.onEach { }.launchIn(this)
        assertFalse(vm.isBookmarked.value)

        repository.bookmarked.value = true

        assertTrue(vm.isBookmarked.awaitValue { it })
        job.cancel()
    }

    @Test
    fun `bookmarking an unbookmarked media saves it and emits a message`() = runBlocking {
        val vm = viewModel()
        vm.fetchMediaId(7)
        val job = vm.isBookmarked.onEach { }.launchIn(this)
        val message = async(start = CoroutineStart.UNDISPATCHED) { withTimeout(5_000) { vm.effect.first() } }

        vm.bookmarkMedia(argument).join()

        assertEquals("Media bookmarked", message.await())
        assertEquals(listOf(7), repository.saved.map { it.mediaId })
        assertEquals("Bleach", repository.saved.single().title)
        assertTrue(repository.deleted.isEmpty())
        job.cancel()
    }

    @Test
    fun `bookmarking a bookmarked media removes it and emits a message`() = runBlocking {
        repository.bookmarked.value = true
        val vm = viewModel()
        vm.fetchMediaId(7)
        val job = vm.isBookmarked.onEach { }.launchIn(this)
        vm.isBookmarked.awaitValue { it }
        val message = async(start = CoroutineStart.UNDISPATCHED) { withTimeout(5_000) { vm.effect.first() } }

        vm.bookmarkMedia(argument).join()

        assertEquals("Media unbookmarked", message.await())
        assertEquals(listOf(7), repository.deleted)
        assertTrue(repository.saved.isEmpty())
        job.cancel()
    }

    @Test
    fun `retrying details fetches them again`() = runBlocking {
        repository.detailsResult = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        val job = vm.animeDetails.onEach { }.launchIn(this)
        vm.animeDetails.awaitValue { it is UiState.ErrorException }
        assertEquals(1, repository.detailsCalls)

        vm.retryDetails()

        withTimeout(5_000) { while (repository.detailsCalls < 2) delay(10) }
        job.cancel()
    }

    @Test
    fun `retrying cast and staff does not refetch details`() = runBlocking {
        repository.detailsResult = DomainNetworkState.ErrorNetwork(message = "offline")
        repository.castResult = DomainNetworkState.ErrorNetwork(message = "offline")
        val vm = viewModel()
        val jobs = listOf(
            vm.animeDetails.onEach { }.launchIn(this),
            vm.animeCast.onEach { }.launchIn(this),
            vm.animeStaff.onEach { }.launchIn(this)
        )
        vm.animeCast.awaitValue { it is UiState.ErrorException }
        vm.animeDetails.awaitValue { it is UiState.ErrorException }

        vm.retryCastAndStaff()

        withTimeout(5_000) { while (repository.castCalls < 2) delay(10) }
        assertEquals(1, repository.detailsCalls)
        jobs.forEach { it.cancel() }
    }
}
