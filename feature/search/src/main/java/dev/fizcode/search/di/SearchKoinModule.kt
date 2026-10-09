package dev.fizcode.search.di

import dev.fizcode.search.data.mapper.SearchDomainMapper
import dev.fizcode.search.data.repository.SearchRepositoryImpl
import dev.fizcode.search.domain.repository.SearchRepository
import dev.fizcode.search.domain.usecase.SearchAnimeUseCase
import dev.fizcode.search.presentation.SearchViewModel
import dev.fizcode.search.presentation.mapper.SearchUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun searchKoinModule() = module {
    includes(
        searchViewModelModule(),
        searchUiMapperModule(),
        searchUseCaseModule(),
        searchDomainMapperModule(),
        searchRepositoryModule()
    )
}

private fun searchViewModelModule() = module {
    viewModelOf(::SearchViewModel)
}

private fun searchUiMapperModule() = module {
    singleOf(::SearchUiMapper)
}

private fun searchUseCaseModule() = module {
    singleOf(::SearchAnimeUseCase)
}

private fun searchDomainMapperModule() = module {
    singleOf(::SearchDomainMapper)
}

private fun searchRepositoryModule() = module {
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
}
