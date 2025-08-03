package dev.fizcode.mediadetails.data.service

import dev.fizcode.common.util.CommonConstant
import dev.fizcode.mediadetails.data.response.JikanAnimeDetailsResponse
import dev.fizcode.mediadetails.data.response.JikanStaffResponse
import dev.fizcode.mediadetails.data.response.JikanVoiceActorsResponse
import dev.fizcode.mediadetails.data.response.MalAnimeDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class MediaDetailsService(
    private val malClient: HttpClient,
    private val jikanClient: HttpClient
) {

    suspend fun fetchMalAnimeDetail(
        animeId: Int,
        fields: String
    ): MalAnimeDetailsResponse =
        malClient.get("anime/${animeId}") {
            parameter(CommonConstant.FIELDS, fields)
        }.body()

    suspend fun fetchJikanAnimeDetail(
        animeId: Int
    ): JikanAnimeDetailsResponse =
        jikanClient.get("anime/${animeId}/full").body()

    suspend fun fetchJikanVoiceActors(
        animeId: Int
    ): JikanVoiceActorsResponse =
        jikanClient.get("anime/${animeId}/characters").body()

    suspend fun fetchJikanStaff(
        animeId: Int
    ): JikanStaffResponse =
        jikanClient.get("anime/${animeId}/staff").body()
}
