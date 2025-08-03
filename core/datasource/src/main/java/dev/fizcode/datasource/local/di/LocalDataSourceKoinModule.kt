package dev.fizcode.datasource.local.di

import dev.fizcode.datasource.local.AniMangaDatabase
import org.koin.dsl.module

fun localDataSourceKoinModule() = module {
    includes(
        databaseModule()
    )
}

private fun databaseModule() = module {
    single { AniMangaDatabase.getInstance(get()) }
}
