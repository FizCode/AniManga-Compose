package dev.fizcode.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChuckerClientHiltModule {
    @Provides
    @Singleton
    fun provideCuckerInterceptorModule(@ApplicationContext context: Context): ChuckerInterceptor =
        ChuckerInterceptor
            .Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250_000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(true)
            .build()

    @Provides
    @Singleton
    fun provideChuckerOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(chuckerInterceptor)
            .build()
}
