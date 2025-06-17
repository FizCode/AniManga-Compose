package dev.fizcode.animanga.di

import dev.fizcode.dashboard.di.dashboardKoinModule
import dev.fizcode.datasource.remote.di.remoteDataSourceKoinModule
import dev.fizcode.mediadetails.di.mediaDetailsKoinModule
import dev.fizcode.network.di.networkModule
import org.koin.dsl.module

internal fun appModule() = module {
    includes(
        featureModule(),
        coreModule()
    )
}

internal fun featureModule() = module {
    includes(
        dashboardKoinModule(),
        mediaDetailsKoinModule()
    )
}

internal fun coreModule() = module {
    includes(
        networkModule(),
        remoteDataSourceKoinModule()
    )
}
