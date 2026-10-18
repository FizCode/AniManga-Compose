package dev.fizcode.mediadetails.data.service

import dev.fizcode.mediadetails.data.response.AniListCastResponse
import dev.fizcode.mediadetails.data.response.AniListDetailsResponse
import dev.fizcode.mediadetails.data.response.AniListStaffResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/**
 * AniList GraphQL API: cast, staff and the extra details MyAnimeList does not provide.
 * Media are looked up by their MyAnimeList id, so the same id works for every source.
 */
internal class AniListService(
    private val client: HttpClient
) {

    suspend fun fetchDetails(malId: Int): AniListDetailsResponse = query(DETAILS_QUERY, malId)

    suspend fun fetchMangaDetails(malId: Int): AniListDetailsResponse =
        query(MANGA_DETAILS_QUERY, malId)

    suspend fun fetchMangaCharacters(malId: Int): AniListCastResponse =
        query(MANGA_CHARACTERS_QUERY, malId)

    suspend fun fetchCast(malId: Int): AniListCastResponse = query(CAST_QUERY, malId)

    suspend fun fetchStaff(malId: Int): AniListStaffResponse = query(STAFF_QUERY, malId)

    /** One page of the characters of an anime, [perPage] is at most 50 for AniList. */
    suspend fun fetchCastPage(malId: Int, page: Int, perPage: Int): AniListCastResponse =
        query(CAST_PAGE_QUERY, malId, page, perPage)

    suspend fun fetchStaffPage(malId: Int, page: Int, perPage: Int): AniListStaffResponse =
        query(STAFF_PAGE_QUERY, malId, page, perPage)

    suspend fun fetchMangaCharactersPage(malId: Int, page: Int, perPage: Int): AniListCastResponse =
        query(MANGA_CHARACTERS_PAGE_QUERY, malId, page, perPage)

    private suspend inline fun <reified Response> query(
        query: String,
        malId: Int,
        page: Int? = null,
        perPage: Int? = null
    ): Response =
        client.post(GRAPHQL_PATH) {
            contentType(ContentType.Application.Json)
            setBody(
                GraphQlRequest(
                    query = query,
                    variables = GraphQlVariables(idMal = malId, page = page, perPage = perPage)
                )
            )
        }.body()

    @Serializable
    private data class GraphQlRequest(
        val query: String,
        val variables: GraphQlVariables
    )

    @Serializable
    private data class GraphQlVariables(
        val idMal: Int,
        // Left out of the request when null, the queries without a page do not declare them.
        val page: Int? = null,
        val perPage: Int? = null
    )

    private companion object {
        const val GRAPHQL_PATH = "/"

        const val PERSON_FIELDS = "id siteUrl name { full } image { medium }"

        const val DETAILS_QUERY = """
            query(${'$'}idMal: Int) {
              Media(idMal: ${'$'}idMal, type: ANIME) {
                studios { nodes { name siteUrl isAnimationStudio } }
                tags { name isMediaSpoiler }
              }
            }
        """

        const val MANGA_DETAILS_QUERY = """
            query(${'$'}idMal: Int) {
              Media(idMal: ${'$'}idMal, type: MANGA) {
                favourites
                tags { name isMediaSpoiler }
              }
            }
        """

        const val MANGA_CHARACTERS_QUERY = """
            query(${'$'}idMal: Int) {
              Media(idMal: ${'$'}idMal, type: MANGA) {
                characters(perPage: 25, sort: [ROLE, RELEVANCE]) {
                  edges {
                    role
                    node { $PERSON_FIELDS }
                  }
                }
              }
            }
        """

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

        const val MANGA_CHARACTERS_PAGE_QUERY = """
            query(${'$'}idMal: Int, ${'$'}page: Int, ${'$'}perPage: Int) {
              Media(idMal: ${'$'}idMal, type: MANGA) {
                characters(page: ${'$'}page, perPage: ${'$'}perPage, sort: [ROLE, RELEVANCE]) {
                  pageInfo { hasNextPage }
                  edges {
                    role
                    node { $PERSON_FIELDS }
                  }
                }
              }
            }
        """

        const val CAST_PAGE_QUERY = """
            query(${'$'}idMal: Int, ${'$'}page: Int, ${'$'}perPage: Int) {
              Media(idMal: ${'$'}idMal, type: ANIME) {
                characters(page: ${'$'}page, perPage: ${'$'}perPage, sort: [ROLE, RELEVANCE]) {
                  pageInfo { hasNextPage }
                  edges {
                    role
                    node { $PERSON_FIELDS }
                    voiceActors(language: JAPANESE) { $PERSON_FIELDS }
                  }
                }
              }
            }
        """

        const val STAFF_PAGE_QUERY = """
            query(${'$'}idMal: Int, ${'$'}page: Int, ${'$'}perPage: Int) {
              Media(idMal: ${'$'}idMal, type: ANIME) {
                staff(page: ${'$'}page, perPage: ${'$'}perPage, sort: [RELEVANCE]) {
                  pageInfo { hasNextPage }
                  edges {
                    role
                    node { $PERSON_FIELDS }
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
