package dev.fizcode.animanga.di

import dev.fizcode.anime.di.animeKoinModule
import dev.fizcode.bookmark.di.bookmarkKoinModule
import dev.fizcode.datasource.local.di.localDataSourceKoinModule
import dev.fizcode.datasource.remote.di.remoteDataSourceKoinModule
import dev.fizcode.mediadetails.di.mediaDetailsKoinModule
import dev.fizcode.network.di.networkModule
import dev.fizcode.search.di.searchKoinModule
import dev.fizcode.seasonal.di.seasonalKoinModule
import org.koin.dsl.module

internal fun appModule() = module {
    includes(
        featureModule(),
        coreModule()
    )
}

internal fun featureModule() = module {
    includes(
        animeKoinModule(),
        mediaDetailsKoinModule(),
        bookmarkKoinModule(),
        seasonalKoinModule(),
        searchKoinModule()
    )
}

internal fun coreModule() = module {
    includes(
        localDataSourceKoinModule(),
        networkModule(),
        remoteDataSourceKoinModule()
    )
}
