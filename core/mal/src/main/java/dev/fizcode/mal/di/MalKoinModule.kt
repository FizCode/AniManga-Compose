package dev.fizcode.mal.di

import dev.fizcode.common.util.DiConstant
import dev.fizcode.mal.service.MalAnimeService
import dev.fizcode.mal.service.MalMangaService
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun malKoinModule() = module {
    factory { MalAnimeService(get(named(DiConstant.NAMED_MAL))) }
    factory { MalMangaService(get(named(DiConstant.NAMED_MAL))) }
}
