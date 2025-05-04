package dev.fizcode.datasource.remote.service

import dev.fizcode.datasource.remote.response.CurrentSeasonAnimeResponse
import dev.fizcode.datasource.remote.response.TopAiringAnimeResponse
import dev.fizcode.datasource.remote.response.TopRankingResponse
import dev.fizcode.datasource.remote.utils.Constant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DashboardAnimeService(private val client: HttpClient) {

    suspend fun fetchSeasonAnime(
        year: Int,
        season: String,
        sortBy: String,
        limit: Int,
        fields: String
    ): CurrentSeasonAnimeResponse =
        client.get("anime/season/${year}/${season}") {
            parameter(Constant.SORT, sortBy)
            parameter(Constant.LIMIT, limit)
            parameter(Constant.FIELDS, fields)
        }.body()

    suspend fun fetchTopAiringAnime(
        rankingType: String,
        limit: Int,
        fields: String
    ): TopAiringAnimeResponse =
        client.get("anime/ranking") {
            parameter(Constant.RANKING_TYPE, rankingType)
            parameter(Constant.LIMIT, limit)
            parameter(Constant.FIELDS, fields)
        }.body()

    suspend fun fetchTopRankingAnime(
        rankingType: String,
        limit: Int,
        fields: String
    ): TopRankingResponse =
        client.get("anime/ranking") {
            parameter(Constant.RANKING_TYPE, rankingType)
            parameter(Constant.LIMIT, limit)
            parameter(Constant.FIELDS, fields)
        }.body()

}
