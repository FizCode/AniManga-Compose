package dev.fizcode.datasource.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.fizcode.datasource.remote.service.DashboardAnimeService
import dev.fizcode.network.di.MalClient
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceHiltModule {
    @Provides
    @Singleton
    fun provideDashboardAnimeService(@MalClient client: HttpClient): DashboardAnimeService =
        DashboardAnimeService(client = client)
}
