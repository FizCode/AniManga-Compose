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
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
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
