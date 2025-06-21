package dev.fizcode.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient

fun provideCuckerInterceptorModule(context: Context): ChuckerInterceptor =
    ChuckerInterceptor
        .Builder(context)
        .collector(ChuckerCollector(context))
        .maxContentLength(250_000L)
        .redactHeaders(emptySet())
        .alwaysReadResponseBody(true)
        .build()

fun provideChuckerOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient =
    OkHttpClient
        .Builder()
        .addInterceptor(chuckerInterceptor)
        .build()
