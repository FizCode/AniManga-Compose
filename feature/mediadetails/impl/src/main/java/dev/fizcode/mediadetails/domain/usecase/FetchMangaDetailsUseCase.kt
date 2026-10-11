package dev.fizcode.mediadetails.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.util.MangaFieldsConstant
import dev.fizcode.common.util.extensions.fieldsPicker
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.repository.MangaDetailsRepository

internal class FetchMangaDetailsUseCase(
    private val mangaDetailsRepository: MangaDetailsRepository
) {
    suspend operator fun invoke(mangaId: Int): DomainNetworkState<MangaDetailsDomainModel> {
        val fields = fieldsPicker(
            MangaFieldsConstant.PICTURES,
            MangaFieldsConstant.MEDIA_TYPE,
            MangaFieldsConstant.STATUS,
            MangaFieldsConstant.NUM_VOLUMES,
            MangaFieldsConstant.NUM_CHAPTERS,
            MangaFieldsConstant.AUTHORS,
            MangaFieldsConstant.SERIALIZATION,
            MangaFieldsConstant.RANK,
            MangaFieldsConstant.POPULARITY,
            MangaFieldsConstant.NUM_LIST_USERS,
            MangaFieldsConstant.MEAN,
            MangaFieldsConstant.NUM_SCORING_USERS,
            MangaFieldsConstant.GENRES,
            MangaFieldsConstant.ALTERNATIVE_TITLES,
            MangaFieldsConstant.SYNOPSIS,
            MangaFieldsConstant.BACKGROUND,
            MangaFieldsConstant.START_DATE,
            MangaFieldsConstant.END_DATE,
            MangaFieldsConstant.RELATED_MANGA
        )

        return mangaDetailsRepository.fetchMangaDetails(mangaId = mangaId, fields = fields)
    }
}
