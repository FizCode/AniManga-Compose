package dev.fizcode.animanga.di

import dev.fizcode.anime.di.animeKoinModule
import dev.fizcode.bookmark.di.bookmarkKoinModule
import dev.fizcode.database.di.databaseKoinModule
import dev.fizcode.mal.di.malKoinModule
import dev.fizcode.datastore.di.DatastoreKoinModule
import dev.fizcode.manga.di.mangaKoinModule
import dev.fizcode.mediadetails.di.mediaDetailsKoinModule
import dev.fizcode.network.di.networkModule
import dev.fizcode.onboarding.di.onBoardingKoinModule
import dev.fizcode.ranking.di.rankingKoinModule
import dev.fizcode.search.di.searchKoinModule
import dev.fizcode.seasonal.di.seasonalKoinModule
import dev.fizcode.settings.di.settingsKoinModule
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
        mangaKoinModule(),
        onBoardingKoinModule(),
        rankingKoinModule(),
        searchKoinModule(),
        settingsKoinModule()
    )
}

internal fun coreModule() = module {
    includes(
        DatastoreKoinModule,
        databaseKoinModule(),
        networkModule(),
        malKoinModule()
    )
}
