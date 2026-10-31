package dev.fizcode.anime.domain

import dev.fizcode.anime.domain.model.SeasonalAnimeDomainModel
import dev.fizcode.anime.domain.model.TopAiringDomainModel
import dev.fizcode.anime.domain.model.TopRankingDomainModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.anime.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopAiringAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopRankingAnimeUseCase
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.mal.util.Season
import dev.fizcode.mal.util.SeasonHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnimeUseCaseTest {

    private class FakeAnimeRepository : AnimeRepository {
        var seasonArgs: List<Any>? = null
        var airingArgs: List<Any>? = null
        var rankingArgs: List<Any>? = null

        val seasonResult = DomainNetworkState.Success(SeasonalAnimeDomainModel())
        val airingResult = DomainNetworkState.Success(TopAiringDomainModel())
        val rankingResult = DomainNetworkState.Success(TopRankingDomainModel())

        override suspend fun fetchSeasonAnime(
            year: Int,
            season: String,
            sortBy: String,
            limit: Int
        ): DomainNetworkState<SeasonalAnimeDomainModel> {
            seasonArgs = listOf(year, season, sortBy, limit)
            return seasonResult
        }

        override suspend fun fetchTopAiringAnime(
            rankingType: String,
            limit: Int
        ): DomainNetworkState<TopAiringDomainModel> {
            airingArgs = listOf(rankingType, limit)
            return airingResult
        }

        override suspend fun fetchTopRankingAnime(
            rankingType: String,
            limit: Int
        ): DomainNetworkState<TopRankingDomainModel> {
            rankingArgs = listOf(rankingType, limit)
            return rankingResult
        }
    }

    private class FakeSeasonHelper : SeasonHelper {
        override fun getCurrentSeason() = Season.FALL
        override fun getCurrentYearForSeason() = 2025
        override fun getCurrentYear() = 2025
    }

    private val repository = FakeAnimeRepository()

    @Test
    fun `seasonal use case uses the current year and season`() = runTest {
        val result = FetchSeasonalAnimeUseCase(repository, FakeSeasonHelper())(limit = 5)

        val args = repository.seasonArgs!!
        assertEquals(2025, args[0])
        assertEquals("fall", args[1])
        assertEquals(5, args[3])
        assertEquals(repository.seasonResult, result)
    }

    @Test
    fun `top airing use case forwards the limit`() = runTest {
        val result = FetchTopAiringAnimeUseCase(repository)(limit = 5)

        val args = repository.airingArgs!!
        assertEquals(5, args[1])
        assertEquals(repository.airingResult, result)
    }

    @Test
    fun `top ranking use case forwards the limit`() = runTest {
        val result = FetchTopRankingAnimeUseCase(repository)(limit = 10)

        val args = repository.rankingArgs!!
        assertEquals(10, args[1])
        assertEquals(repository.rankingResult, result)
    }

    @Test
    fun `different ranking types are used for airing and overall ranking`() = runTest {
        FetchTopAiringAnimeUseCase(repository)(limit = 1)
        FetchTopRankingAnimeUseCase(repository)(limit = 1)

        assertTrue(repository.airingArgs!![0] != repository.rankingArgs!![0])
    }
}
