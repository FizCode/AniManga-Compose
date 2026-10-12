package dev.fizcode.bookmark.di

import dev.fizcode.bookmark.data.mapper.BookmarkDomainMapper
import dev.fizcode.bookmark.data.repository.BookmarkRepositoryImpl
import dev.fizcode.bookmark.domain.repository.BookmarkRepository
import dev.fizcode.bookmark.presentation.BookmarkViewModel
import dev.fizcode.bookmark.presentation.mapper.BookmarkUiMapper
import dev.fizcode.datasource.local.AniMangaDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun bookmarkKoinModule() = module {
    includes(
        bookmarkViewModelModule(),
        bookmarkUiMapperModule(),
        bookmarkDomainMapperModule(),
        bookmarkRepositoryModule()
    )
}

private fun bookmarkViewModelModule() = module {
    viewModelOf(::BookmarkViewModel)
}

private fun bookmarkUiMapperModule() = module {
    factoryOf(::BookmarkUiMapper)
}

private fun bookmarkDomainMapperModule() = module {
    factoryOf(::BookmarkDomainMapper)
}

private fun bookmarkRepositoryModule() = module {
    single<BookmarkRepository> {
        BookmarkRepositoryImpl(
            bookmarkDAO = get<AniMangaDatabase>().bookmarkDao(),
            bookmarkDomainMapper = get()
        )
    }
}