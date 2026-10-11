package dev.fizcode.datastore.di

import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.datastore.AniMangaDatastore
import dev.fizcode.datastore.DatastoreNsfwFilterProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DatastoreKoinModule = module {
    singleOf(::AniMangaDatastore)
    singleOf(::DatastoreNsfwFilterProvider) bind NsfwFilterProvider::class
}
