package dev.fizcode.mal.di

import dev.fizcode.network.di.NetworkQualifier
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.mal.service.MalMangaService
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun malKoinModule() = module {
    factory { MalAnimeService(get(named(NetworkQualifier.MAL))) }
    factory { MalMangaService(get(named(NetworkQualifier.MAL))) }
}
