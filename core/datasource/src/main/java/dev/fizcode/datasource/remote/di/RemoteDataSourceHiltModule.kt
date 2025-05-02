package dev.fizcode.datasource.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.fizcode.datasource.remote.service.DashboardAnimeService
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceHiltModule {
    @Provides
    @Singleton
    fun provideDashboardAnimeService(client: HttpClient): DashboardAnimeService =
        DashboardAnimeService(client = client)
}
