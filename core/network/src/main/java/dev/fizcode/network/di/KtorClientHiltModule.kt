package dev.fizcode.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.fizcode.network.BuildConfig
import dev.fizcode.network.util.Constant
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class KtorClientHiltModule {

    @Provides
    @Singleton
    fun provideKtorHttpClientModule(okHttpClient: OkHttpClient): HttpClient =
        HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpClient
            }
            expectSuccess = false

            install(ContentNegotiation) {
                json(
                    Json {
                        coerceInputValues = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpCache)

            install(DefaultRequest) {
                header(Constant.HEADER_MAL_CLIENT_ID, BuildConfig.X_MAL_CLIENT_ID)
                url(Constant.BASE_URL)
            }
        }

}
