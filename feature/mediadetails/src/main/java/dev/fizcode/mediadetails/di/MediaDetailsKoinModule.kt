package dev.fizcode.mediadetails.di

import dev.fizcode.common.util.DiConstant
import dev.fizcode.datasource.local.AniMangaDatabase
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.mapper.BookmarkDomainMapper
import dev.fizcode.mediadetails.data.repository.MediaDetailsRepositoryImpl
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.MediaDetailsViewModel
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkUiMapper
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun mediaDetailsKoinModule() = module {
    includes(
        mediaDetailsViewModelModule(),
        mediaDetailsUiMapperModule(),
        mediaDetailsUseCaseModule(),
        mediaDetailsDomainMapperModule(),
        mediaDetailsRepositoryModule(),
        mediaDetailsServiceModule()
    )
}

private fun mediaDetailsViewModelModule() = module {
    viewModelOf(::MediaDetailsViewModel)
}

private fun mediaDetailsUiMapperModule() = module {
    singleOf(::AnimeDetailsUiMapper)
    singleOf(::AnimeBookmarkUiMapper)
}

private fun mediaDetailsUseCaseModule() = module {
    singleOf(::FetchAnimeDetailsUseCase)
}

private fun mediaDetailsDomainMapperModule() = module {
    singleOf(::AnimeDetailsDomainMapper)
    singleOf(::BookmarkDomainMapper)
}

private fun mediaDetailsRepositoryModule() = module {
    single<MediaDetailsRepository> {
        MediaDetailsRepositoryImpl(
            animeDetailsService = get(),
            bookmarkDao = get<AniMangaDatabase>().bookmarkDao(),
            animeDetailsDomainMapper = get(),
            bookmarkDomainMapper = get(),
        )
    }
}

private fun mediaDetailsServiceModule() = module {
    factory {
        MediaDetailsService(
            get(named(DiConstant.NAMED_MAL)),
            get(named(DiConstant.NAMED_JIKAN))
        )
    }
}
