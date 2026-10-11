package dev.fizcode.mediadetails.di

import dev.fizcode.common.util.DiConstant
import dev.fizcode.datasource.local.AniMangaDatabase
import dev.fizcode.mediadetails.data.mapper.AniListDomainMapper
import dev.fizcode.mediadetails.data.mapper.AnimeDetailsDomainMapper
import dev.fizcode.mediadetails.data.mapper.BookmarkDomainMapper
import dev.fizcode.mediadetails.data.mapper.MangaDetailsDomainMapper
import dev.fizcode.mediadetails.data.repository.MangaDetailsRepositoryImpl
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchMangaDetailsUseCase
import dev.fizcode.mediadetails.presentation.mapper.MangaDetailsUiMapper
import dev.fizcode.mediadetails.data.repository.MediaDetailsRepositoryImpl
import dev.fizcode.mediadetails.data.service.AniListService
import dev.fizcode.mediadetails.data.service.MediaDetailsService
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.domain.usecase.FetchAnimeDetailsUseCase
import dev.fizcode.mediadetails.presentation.MediaDetailsViewModel
import dev.fizcode.mediadetails.presentation.mapper.AnimeBookmarkUiMapper
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun mediaDetailsKoinModule() = module {
    includes(
        mediaDetailsViewModelModule(),
        mediaDetailsUiMapperModule(),
        mediaDetailsUseCaseModule(),
        mediaDetailsDomainMapperModule(),
        mediaDetailsRepositoryModule(),
        mediaDetailsMangaRepositoryModule(),
        mediaDetailsServiceModule(),
        mediaDetailsAniListModule()
    )
}

private fun mediaDetailsViewModelModule() = module {
    viewModel { (mediaId: Int) ->
        MediaDetailsViewModel(
            mediaId = mediaId,
            animeDetailsUseCase = get(),
            animeRepository = get(),
            animeDetailsUiMapper = get(),
            animeBookmarkUiMapper = get(),
            mangaDetailsUseCase = get(),
            mangaRepository = get(),
            mangaDetailsUiMapper = get()
        )
    }
}

private fun mediaDetailsUiMapperModule() = module {
    singleOf(::AnimeDetailsUiMapper)
    singleOf(::AnimeBookmarkUiMapper)
    singleOf(::MangaDetailsUiMapper)
}

private fun mediaDetailsUseCaseModule() = module {
    singleOf(::FetchAnimeDetailsUseCase)
    singleOf(::FetchMangaDetailsUseCase)
}

private fun mediaDetailsDomainMapperModule() = module {
    singleOf(::AnimeDetailsDomainMapper)
    singleOf(::BookmarkDomainMapper)
    singleOf(::AniListDomainMapper)
    singleOf(::MangaDetailsDomainMapper)
}

private fun mediaDetailsRepositoryModule() = module {
    single<MediaDetailsRepository> {
        MediaDetailsRepositoryImpl(
            animeDetailsService = get(),
            aniListService = get(),
            bookmarkDao = get<AniMangaDatabase>().bookmarkDao(),
            animeDetailsDomainMapper = get(),
            aniListDomainMapper = get(),
            bookmarkDomainMapper = get(),
        )
    }
}

private fun mediaDetailsMangaRepositoryModule() = module {
    single<MangaDetailsRepository> {
        MangaDetailsRepositoryImpl(
            detailsService = get(),
            aniListService = get(),
            mangaDetailsDomainMapper = get(),
            aniListDomainMapper = get()
        )
    }
}

private fun mediaDetailsServiceModule() = module {
    factory { MediaDetailsService(get(named(DiConstant.NAMED_MAL))) }
}

private fun mediaDetailsAniListModule() = module {
    factory { AniListService(get(named(DiConstant.NAMED_ANILIST))) }
}
