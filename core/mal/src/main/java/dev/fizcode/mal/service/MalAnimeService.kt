package dev.fizcode.mal.service

import dev.fizcode.common.util.CommonConstant
import dev.fizcode.mal.response.MalAnimeNode
import dev.fizcode.mal.response.MalListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * The anime list endpoints of MyAnimeList. [nsfw] asks for NSFW media too, MyAnimeList leaves them
 * out otherwise.
 */
class MalAnimeService(private val malClient: HttpClient) {

    suspend fun fetchSeason(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int,
        fields: String,
        nsfw: Boolean = false
    ): MalListResponse<MalAnimeNode> =
        malClient.get("anime/season/${year}/${season}") {
            parameter(CommonConstant.SORT, sortBy)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
            if (nsfw) parameter(CommonConstant.NSFW, true)
        }.body()

    suspend fun fetchRanking(
        rankingType: String,
        limit: Int,
        fields: String,
        nsfw: Boolean = false
    ): MalListResponse<MalAnimeNode> =
        malClient.get("anime/ranking") {
            parameter(CommonConstant.RANKING_TYPE, rankingType)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
            if (nsfw) parameter(CommonConstant.NSFW, true)
        }.body()

    suspend fun search(
        query: String,
        limit: Int,
        fields: String,
        nsfw: Boolean = false
    ): MalListResponse<MalAnimeNode> =
        malClient.get("anime") {
            parameter(CommonConstant.QUERY, query)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
            if (nsfw) parameter(CommonConstant.NSFW, true)
        }.body()
}
