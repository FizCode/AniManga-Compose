package dev.fizcode.manga.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.util.MangaFieldsConstant
import dev.fizcode.common.util.extensions.fieldsPicker
import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.domain.repository.MangaRepository

internal class FetchMangaRankingUseCase(
    private val mangaRepository: MangaRepository
) {
    suspend operator fun invoke(
        rankingType: String,
        limit: Int
    ): DomainNetworkState<MangaDomainModel> {
        val fields = fieldsPicker(
            MangaFieldsConstant.ALTERNATIVE_TITLES,
            MangaFieldsConstant.NSFW,
            MangaFieldsConstant.MEDIA_TYPE,
            MangaFieldsConstant.MEAN,
            MangaFieldsConstant.STATUS,
            MangaFieldsConstant.NUM_VOLUMES,
            MangaFieldsConstant.NUM_CHAPTERS,
            MangaFieldsConstant.SYNOPSIS,
            MangaFieldsConstant.GENRES
        )

        return mangaRepository.fetchMangaRanking(
            rankingType = rankingType,
            limit = limit,
            fields = fields
        )
    }
}
