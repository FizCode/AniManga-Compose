package dev.fizcode.datasource.remote.service

import dev.fizcode.common.util.CommonConstant
import dev.fizcode.datasource.remote.response.MangaRankingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MangaService(private val malClient: HttpClient) {

    suspend fun fetchMangaRanking(
        rankingType: String,
        limit: Int,
        fields: String,
        nsfw: Boolean = false
    ): MangaRankingResponse =
        malClient.get("manga/ranking") {
            parameter(CommonConstant.RANKING_TYPE, rankingType)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
            if (nsfw) parameter(CommonConstant.NSFW, true)
        }.body()

}
