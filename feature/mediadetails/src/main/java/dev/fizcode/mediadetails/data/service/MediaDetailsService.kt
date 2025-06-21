package dev.fizcode.mediadetails.data.service

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
        animeId: Int
    ): MalAnimeDetailsResponse =
        malClient.get("anime/${animeId}") {
            parameter(
                "fields",
                "pictures, media_type, status, start_season, num_episodes, average_episode_duration, studios, rank, popularity, num_list_users, mean, num_scoring_users, genres, alternative_titles, synopsis, background, start_date, end_date, broadcast, source, rating, characters"
            )
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
