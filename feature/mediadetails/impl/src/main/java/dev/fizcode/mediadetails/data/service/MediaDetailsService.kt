package dev.fizcode.mediadetails.data.service

import dev.fizcode.common.util.CommonConstant
import dev.fizcode.mediadetails.data.response.MalAnimeDetailsResponse
import dev.fizcode.mediadetails.data.response.MalMangaDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class MediaDetailsService(
    private val malClient: HttpClient
) {

    suspend fun fetchMalAnimeDetail(
        animeId: Int,
        fields: String
    ): MalAnimeDetailsResponse =
        malClient.get("anime/${animeId}") {
            parameter(CommonConstant.FIELDS, fields)
        }.body()

    suspend fun fetchMalMangaDetail(
        mangaId: Int,
        fields: String
    ): MalMangaDetailsResponse =
        malClient.get("manga/${mangaId}") {
            parameter(CommonConstant.FIELDS, fields)
        }.body()
}
