package dev.fizcode.ranking.di

import dev.fizcode.ranking.data.mapper.RankingDomainMapper
import dev.fizcode.ranking.data.repository.RankingRepositoryImpl
import dev.fizcode.ranking.domain.repository.RankingRepository
import dev.fizcode.ranking.domain.usecase.FetchAnimeRankingUseCase
import dev.fizcode.ranking.presentation.RankingViewModel
import dev.fizcode.ranking.presentation.mapper.RankingUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

fun rankingKoinModule() = module {
    includes(
        rankingViewModelModule(),
        rankingUiMapperModule(),
        rankingUseCaseModule(),
        rankingDomainMapperModule(),
        rankingRepositoryModule()
    )
}

private fun rankingViewModelModule() = module {
    // The screen decides which ranking it shows by passing the MAL ranking type.
    viewModel { (rankingType: String) ->
        RankingViewModel(
            rankingType = rankingType,
            fetchAnimeRankingUseCase = get(),
            rankingUiMapper = get(),
            nsfwFilterProvider = get()
        )
    }
}

private fun rankingUiMapperModule() = module {
    singleOf(::RankingUiMapper)
}

private fun rankingUseCaseModule() = module {
    singleOf(::FetchAnimeRankingUseCase)
}

private fun rankingDomainMapperModule() = module {
    singleOf(::RankingDomainMapper)
}

private fun rankingRepositoryModule() = module {
    singleOf(::RankingRepositoryImpl) bind RankingRepository::class
}
