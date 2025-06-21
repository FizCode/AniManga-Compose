package dev.fizcode.datastore.di

import dev.fizcode.datastore.AniMangaDatastore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DatastoreKoinModule = module {
    singleOf(::AniMangaDatastore)
}
