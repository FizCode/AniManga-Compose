package dev.fizcode.network.di

import dev.fizcode.network.util.Constant
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun networkModule() = module {
    singleOf(::provideCuckerInterceptorModule)
    singleOf(::provideChuckerOkHttpClient)
    single(named(Constant.NAMED_MAL)) { provideMalKtorHttpClientModule(get()) }
    single(named(Constant.NAMED_JIKAN)) { provideJikanKtorHttpClient(get()) }
}
