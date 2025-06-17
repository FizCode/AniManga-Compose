package dev.fizcode.mediadetails.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.repository.MediaDetailsRepositoryImpl
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.network.di.JikanClient
import dev.fizcode.network.di.MalClient
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface MediaDetailsHiltRepoModule {
    @Binds
    fun bindAnimeDetailsRepositoryModule(
        repository: MediaDetailsRepositoryImpl
    ): MediaDetailsRepository
}

@Module
@InstallIn(SingletonComponent::class)
internal object MediaDetailsHiltModule {

    @Provides
    @Singleton
    fun animeDetailsUseCase(
        mediaDetailsRepository: MediaDetailsRepository
    ) = FetchAnimeDetailsUseCase(
        mediaDetailsRepository = mediaDetailsRepository
    )

    @Provides
    @Singleton
    fun animeDetailsDomainMapperModule(): AnimeDetailsDomainMapper = AnimeDetailsDomainMapper()

    @Provides
    @Singleton
    fun animeDetailsUiMapperMapperModule(): AnimeDetailsUiMapper = AnimeDetailsUiMapper()

    @Provides
    @Singleton
    fun provideMediaDetailsService(
        @MalClient malClient: HttpClient,
        @JikanClient jikanClient: HttpClient
    ): MediaDetailsService = MediaDetailsService(
        malClient = malClient,
        jikanClient = jikanClient
    )

}
