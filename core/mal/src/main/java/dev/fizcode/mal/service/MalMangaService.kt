package dev.fizcode.mal.service

import dev.fizcode.common.util.CommonConstant
import dev.fizcode.mal.response.MalListResponse
import dev.fizcode.mal.response.MalMangaNode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** The manga list endpoints of MyAnimeList, see [MalAnimeService] for [nsfw]. */
class MalMangaService(private val malClient: HttpClient) {

    suspend fun fetchRanking(
        rankingType: String,
        limit: Int,
        fields: String,
        nsfw: Boolean = false
    ): MalListResponse<MalMangaNode> =
        malClient.get("manga/ranking") {
            parameter(CommonConstant.RANKING_TYPE, rankingType)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
            if (nsfw) parameter(CommonConstant.NSFW, true)
        }.body()
}
