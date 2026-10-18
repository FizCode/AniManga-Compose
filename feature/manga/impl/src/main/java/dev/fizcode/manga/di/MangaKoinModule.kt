package dev.fizcode.manga.di

import dev.fizcode.manga.data.mapper.MangaDomainMapper
import dev.fizcode.manga.data.repository.MangaRepositoryImpl
import dev.fizcode.manga.domain.repository.MangaRepository
import dev.fizcode.manga.domain.usecase.FetchMangaRankingUseCase
import dev.fizcode.manga.presentation.MangaViewModel
import dev.fizcode.manga.presentation.mapper.MangaUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun mangaKoinModule() = module {
    includes(
        mangaViewModelModule(),
        mangaUiMapperModule(),
        mangaUseCaseModule(),
        mangaDomainMapperModule(),
        mangaRepositoryModule()
    )
}

private fun mangaViewModelModule() = module {
    viewModelOf(::MangaViewModel)
}

private fun mangaUiMapperModule() = module {
    singleOf(::MangaUiMapper)
}

private fun mangaUseCaseModule() = module {
    singleOf(::FetchMangaRankingUseCase)
}

private fun mangaDomainMapperModule() = module {
    singleOf(::MangaDomainMapper)
}

private fun mangaRepositoryModule() = module {
    singleOf(::MangaRepositoryImpl) bind MangaRepository::class
}
