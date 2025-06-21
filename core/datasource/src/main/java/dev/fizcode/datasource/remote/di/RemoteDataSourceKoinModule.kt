package dev.fizcode.datasource.remote.di

import dev.fizcode.common.util.DiConstant
import dev.fizcode.datasource.remote.service.DashboardAnimeService
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun remoteDataSourceKoinModule() = module {
    factory { DashboardAnimeService(get(named(DiConstant.NAMED_MAL))) }
}
