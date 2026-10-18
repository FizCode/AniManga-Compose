package dev.fizcode.mediadetails.presentation.people

import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.StaffDomainModel
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository
import dev.fizcode.mediadetails.domain.repository.MediaDetailsRepository
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.presentation.mapper.AnimeDetailsUiMapper
import dev.fizcode.mediadetails.presentation.mapper.MangaDetailsUiMapper

/** Every character and voice actor of an anime. */
internal class MediaCastViewModel(
    mediaId: Int,
    repository: MediaDetailsRepository,
    uiMapper: AnimeDetailsUiMapper
) : PagedListViewModel<CastDomainModel, AnimeCastUiModel>(
    fetchPage = { page -> repository.fetchAnimeCastPage(mediaId, page, PAGE_SIZE) },
    mapPage = uiMapper::mapToAnimeCastPage
)

/** Every staff member of an anime. */
internal class MediaStaffViewModel(
    mediaId: Int,
    repository: MediaDetailsRepository,
    uiMapper: AnimeDetailsUiMapper
) : PagedListViewModel<StaffDomainModel, AnimeStaffUiModel>(
    fetchPage = { page -> repository.fetchAnimeStaffPage(mediaId, page, PAGE_SIZE) },
    mapPage = uiMapper::mapToStaffPage
)

/** Every character of a manga. */
internal class MangaCharactersViewModel(
    mediaId: Int,
    repository: MangaDetailsRepository,
    uiMapper: MangaDetailsUiMapper
) : PagedListViewModel<CastDomainModel, MangaCharacterUiModel>(
    fetchPage = { page -> repository.fetchMangaCharactersPage(mediaId, page, PAGE_SIZE) },
    mapPage = uiMapper::mapToCharacterPage
)
