package dev.fizcode.database.di

import android.content.Context
import androidx.room.Room
import dev.fizcode.database.AniMangaDatabase
import dev.fizcode.database.bookmark.BookmarkLocalDataSource
import dev.fizcode.database.bookmark.BookmarkLocalDataSourceImpl
import dev.fizcode.database.util.Constant
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun databaseKoinModule() = module {
    single {
        Room.databaseBuilder(
            get<Context>().applicationContext,
            AniMangaDatabase::class.java,
            Constant.ANIMANGA_DB_NAME
        ).build()
    }
    single { get<AniMangaDatabase>().bookmarkDao() }
    singleOf(::BookmarkLocalDataSourceImpl) bind BookmarkLocalDataSource::class
}
