package dev.fizcode.anime.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.fizcode.anime.data.mapper.SeasonalDomainMapper
import dev.fizcode.anime.data.mapper.TopAiringDomainMapper
import dev.fizcode.anime.data.mapper.TopRankingDomainMapper
import dev.fizcode.anime.data.repository.AnimeRepositoryImpl
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.anime.domain.usecase.AnimeUseCaseGroup
import dev.fizcode.anime.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopAiringAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopRankingAnimeUseCase
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
import dev.fizcode.common.util.SeasonHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AnimeHiltRepoModule {
    @Binds
    fun bindAnimeRepositoryModule(repository: AnimeRepositoryImpl): AnimeRepository

}

@Module
@InstallIn(SingletonComponent::class)
internal object AnimeHiltModule {

    @Provides
    @Singleton
    fun animeUseCaseGroupModule(
        seasonalAnimeUseCase: FetchSeasonalAnimeUseCase,
        topAiringAnimeUseCase: FetchTopAiringAnimeUseCase,
        topRankingAnimeUseCase: FetchTopRankingAnimeUseCase
    ) = AnimeUseCaseGroup(
        fetchSeasonAnimeUseCase = seasonalAnimeUseCase,
        fetchTopAiringAnimeUseCase = topAiringAnimeUseCase,
        fetchTopRankingAnimeUseCase = topRankingAnimeUseCase
    )

    @Provides
    @Singleton
    fun seasonalAnimeUseCaseModule(
        animeRepository: AnimeRepository,
        seasonHelper: SeasonHelper
    ) = FetchSeasonalAnimeUseCase(
        animeRepository = animeRepository,
        seasonHelper = seasonHelper
    )

    @Provides
    @Singleton
    fun topAiringAnimeUseCaseModule(
        animeRepository: AnimeRepository
    ) = FetchTopAiringAnimeUseCase(
        animeRepository = animeRepository
    )

    @Provides
    @Singleton
    fun topRankingAnimeUseCaseModule(
        animeRepository: AnimeRepository
    ) = FetchTopRankingAnimeUseCase(
        animeRepository = animeRepository
    )

    @Provides
    @Singleton
    fun provideSeasonHelper(): SeasonHelper = SeasonHelper

    @Provides
    @Singleton
    fun seasonalDomainMapperModule(): SeasonalDomainMapper = SeasonalDomainMapper()

    @Provides
    @Singleton
    fun topAiringDomainMapperModule(): TopAiringDomainMapper = TopAiringDomainMapper()

    @Provides
    @Singleton
    fun topRankingDomainMapperModule(): TopRankingDomainMapper = TopRankingDomainMapper()

    @Provides
    @Singleton
    fun seasonalUiMapperModule(): SeasonalAnimeUiMapper = SeasonalAnimeUiMapper()

    @Provides
    @Singleton
    fun topAiringUiMapperModule(): TopAiringAnimeUiMapper = TopAiringAnimeUiMapper()

    @Provides
    @Singleton
    fun topRankingUiMapperModule(): TopRankingAnimeUiMapper = TopRankingAnimeUiMapper()
}
