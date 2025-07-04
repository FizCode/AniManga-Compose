package dev.fizcode.datasource.remote.service

import dev.fizcode.common.util.CommonConstant
import dev.fizcode.datasource.remote.response.CurrentSeasonAnimeResponse
import dev.fizcode.datasource.remote.response.TopAiringAnimeResponse
import dev.fizcode.datasource.remote.response.TopRankingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DashboardAnimeService(private val malClient: HttpClient) {

    suspend fun fetchSeasonAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int,
        fields: String
    ): CurrentSeasonAnimeResponse =
        malClient.get("anime/season/${year}/${season}") {
            parameter(CommonConstant.SORT, sortBy)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
        }.body()

    suspend fun fetchTopAiringAnime(
        rankingType: String,
        limit: Int,
        fields: String
    ): TopAiringAnimeResponse =
        malClient.get("anime/ranking") {
            parameter(CommonConstant.RANKING_TYPE, rankingType)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
        }.body()

    suspend fun fetchTopRankingAnime(
        rankingType: String,
        limit: Int,
        fields: String
    ): TopRankingResponse =
        malClient.get("anime/ranking") {
            parameter(CommonConstant.RANKING_TYPE, rankingType)
            parameter(CommonConstant.LIMIT, limit)
            parameter(CommonConstant.FIELDS, fields)
        }.body()

}
