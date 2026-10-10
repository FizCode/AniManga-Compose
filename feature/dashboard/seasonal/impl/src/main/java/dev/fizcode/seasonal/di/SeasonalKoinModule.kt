package dev.fizcode.seasonal.di

import dev.fizcode.common.util.DefaultSeasonHelper
import dev.fizcode.common.util.SeasonHelper
import dev.fizcode.seasonal.data.mapper.SeasonalDomainMapper
import dev.fizcode.seasonal.data.repository.SeasonalRepositoryImpl
import dev.fizcode.seasonal.domain.repository.SeasonalRepository
import dev.fizcode.seasonal.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.seasonal.presentation.SeasonalViewModel
import dev.fizcode.seasonal.presentation.mapper.SeasonalUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun seasonalKoinModule() = module {
    includes(
        seasonalViewModelModule(),
        seasonalUiMapperModule(),
        seasonalUseCaseModule(),
        seasonalDomainMapperModule(),
        seasonalRepositoryModule(),
        seasonalHelperModule()
    )
}

private fun seasonalViewModelModule() = module {
    viewModelOf(::SeasonalViewModel)
}

private fun seasonalUiMapperModule() = module {
    singleOf(::SeasonalUiMapper)
}

private fun seasonalUseCaseModule() = module {
    singleOf(::FetchSeasonalAnimeUseCase)
}

private fun seasonalDomainMapperModule() = module {
    singleOf(::SeasonalDomainMapper)
}

private fun seasonalRepositoryModule() = module {
    singleOf(::SeasonalRepositoryImpl) bind SeasonalRepository::class
}

private fun seasonalHelperModule() = module {
    singleOf(::DefaultSeasonHelper) bind SeasonHelper::class
}
