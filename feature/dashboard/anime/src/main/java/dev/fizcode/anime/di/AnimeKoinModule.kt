package dev.fizcode.anime.di

import dev.fizcode.anime.data.mapper.SeasonalDomainMapper
import dev.fizcode.anime.data.mapper.TopAiringDomainMapper
import dev.fizcode.anime.data.mapper.TopRankingDomainMapper
import dev.fizcode.anime.data.repository.AnimeRepositoryImpl
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.anime.domain.usecase.AnimeUseCaseGroup
import dev.fizcode.anime.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopAiringAnimeUseCase
import dev.fizcode.anime.domain.usecase.FetchTopRankingAnimeUseCase
import dev.fizcode.anime.presentation.AnimeViewModel
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
import dev.fizcode.common.util.DefaultSeasonHelper
import dev.fizcode.common.util.SeasonHelper
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun animeKoinModule() = module {
    includes(
        animeViewModelModule(),
        animeUiMapperModule(),
        animeUseCaseModule(),
        animeDomainMapperModule(),
        animeRepositoryModule(),
        animeHelperModule()
    )
}

private fun animeViewModelModule() = module {
    viewModelOf(::AnimeViewModel)
}

private fun animeUiMapperModule() = module {
    singleOf(::SeasonalAnimeUiMapper)
    singleOf(::TopAiringAnimeUiMapper)
    singleOf(::TopRankingAnimeUiMapper)
}

private fun animeUseCaseModule() = module {
    singleOf(::AnimeUseCaseGroup)
    singleOf(::FetchSeasonalAnimeUseCase)
    singleOf(::FetchTopAiringAnimeUseCase)
    singleOf(::FetchTopRankingAnimeUseCase)
}

private fun animeDomainMapperModule() = module {
    singleOf(::SeasonalDomainMapper)
    singleOf(::TopAiringDomainMapper)
    singleOf(::TopRankingDomainMapper)
}

private fun animeRepositoryModule() = module {
    singleOf(::AnimeRepositoryImpl) bind AnimeRepository::class
}

private fun animeHelperModule() = module {
    singleOf(::DefaultSeasonHelper) bind SeasonHelper::class
}
