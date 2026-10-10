package dev.fizcode.animanga.di

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import okhttp3.OkHttpClient
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class AppKoinModuleTest {

    /**
     * Verifies that every definition in [appModule] can resolve its constructor dependencies.
     * Types provided from outside the graph (e.g. the Android context) are listed in `extraTypes`.
     */
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun verifyAppModule() {
        appModule().verify(
            extraTypes = listOf(
                Context::class,
                HttpClient::class,
                HttpClientEngine::class,
                OkHttpClient::class,
            )
        )
    }
}
