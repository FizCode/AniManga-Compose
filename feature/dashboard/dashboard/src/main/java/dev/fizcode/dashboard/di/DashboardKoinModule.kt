package dev.fizcode.dashboard.di

import dev.fizcode.anime.di.animeKoinModule
import org.koin.dsl.module

fun dashboardKoinModule() = module {
    includes(
        animeKoinModule()
    )
}
