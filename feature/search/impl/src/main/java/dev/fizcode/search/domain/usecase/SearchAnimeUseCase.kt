package dev.fizcode.search.domain.usecase

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.util.AnimeFieldsConstant
import dev.fizcode.common.util.extensions.fieldsPicker
import dev.fizcode.search.domain.model.SearchAnimeDomainModel
import dev.fizcode.search.domain.repository.SearchRepository

internal class SearchAnimeUseCase(
    private val searchRepository: SearchRepository
) {

    suspend operator fun invoke(
        query: String,
        limit: Int
    ): DomainNetworkState<SearchAnimeDomainModel> {
        val fields = fieldsPicker(
            AnimeFieldsConstant.MEDIA_TYPE,
            AnimeFieldsConstant.MEAN,
            AnimeFieldsConstant.STATUS,
            AnimeFieldsConstant.NUM_EPISODES,
            AnimeFieldsConstant.GENRES
        )

        return searchRepository.searchAnime(
            query = query,
            limit = limit,
            fields = fields
        )
    }
}
