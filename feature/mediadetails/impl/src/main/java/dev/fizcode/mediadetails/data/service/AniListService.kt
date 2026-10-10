package dev.fizcode.mediadetails.data.service

import dev.fizcode.mediadetails.data.response.AniListCastResponse
import dev.fizcode.mediadetails.data.response.AniListStaffResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/**
 * AniList GraphQL API, used as a fallback for the data Jikan provides.
 * Media are looked up by their MyAnimeList id, so the same id works for every source.
 */
internal class AniListService(
    private val client: HttpClient
) {

    suspend fun fetchCast(malId: Int): AniListCastResponse = query(CAST_QUERY, malId)

    suspend fun fetchStaff(malId: Int): AniListStaffResponse = query(STAFF_QUERY, malId)

    private suspend inline fun <reified Response> query(query: String, malId: Int): Response =
        client.post(GRAPHQL_PATH) {
            contentType(ContentType.Application.Json)
            setBody(GraphQlRequest(query = query, variables = GraphQlVariables(idMal = malId)))
        }.body()

    @Serializable
    private data class GraphQlRequest(
        val query: String,
        val variables: GraphQlVariables
    )

    @Serializable
    private data class GraphQlVariables(
        val idMal: Int
    )

    private companion object {
        const val GRAPHQL_PATH = "/"

        const val PERSON_FIELDS = "id siteUrl name { full } image { medium }"

        const val CAST_QUERY = """
            query(${'$'}idMal: Int) {
              Media(idMal: ${'$'}idMal, type: ANIME) {
                characters(perPage: 25, sort: [ROLE, RELEVANCE]) {
                  edges {
                    role
                    node { $PERSON_FIELDS }
                    voiceActors(language: JAPANESE) { $PERSON_FIELDS }
                  }
                }
              }
            }
        """

        const val STAFF_QUERY = """
            query(${'$'}idMal: Int) {
              Media(idMal: ${'$'}idMal, type: ANIME) {
                staff(perPage: 25, sort: [RELEVANCE]) {
                  edges {
                    role
                    node { $PERSON_FIELDS }
                  }
                }
              }
            }
        """
    }
}
