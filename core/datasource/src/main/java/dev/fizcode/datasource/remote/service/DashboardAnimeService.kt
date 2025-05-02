package dev.fizcode.datasource.remote.service

import dev.fizcode.datasource.remote.response.CurrentSeasonAnimeResponse
import dev.fizcode.datasource.remote.response.TopAiringAnimeResponse
import dev.fizcode.datasource.remote.response.TopRankingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DashboardAnimeService(private val client: HttpClient) {

    suspend fun fetchSeasonAnime(): CurrentSeasonAnimeResponse =
        client.get("anime/season/2017/spring") {
            parameter("limit", 5)
            parameter("fields", "mean,broadcast,status,num_episodes,studios,synopsis,genres")
        }.body()

    suspend fun fetchTopAiringAnime(): TopAiringAnimeResponse =
        client.get("anime/season/2017/spring") {
            parameter("ranking_type", "airing")
            parameter("limit", 5)
            parameter("fields", "mean")
        }.body()

    suspend fun fetchTopRankingAnime(): TopRankingResponse =
        client.get("anime/ranking") {
            parameter("ranking_type", "all")
            parameter("limit", 10)
            parameter("fields", "mean,broadcast,status,media_type,num_episodes,studios,genres")
        }.body()

}
