package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.common.util.extensions.orZero
import dev.fizcode.mediadetails.data.response.AniListCastResponse
import dev.fizcode.mediadetails.data.response.AniListDetailsResponse
import dev.fizcode.mediadetails.data.response.AniListPerson
import dev.fizcode.mediadetails.data.response.AniListStaffResponse
import dev.fizcode.mediadetails.domain.model.AniListAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.AniListMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.StaffDomainModel
import dev.fizcode.mediadetails.util.Constant
import java.net.URLEncoder
import dev.fizcode.mediadetails.domain.model.PagedResult

/**
 * Maps AniList responses to the details, cast and staff domain models.
 *
 * The ids in the cast and staff models are AniList ids, and their urls point to AniList.
 */
internal class AniListDomainMapper {

    fun mapToDetailsDomainModel(response: AniListDetailsResponse): AniListAnimeDetailsDomainModel {
        val media = response.data?.media
        return AniListAnimeDetailsDomainModel(
            producers = media?.studios?.nodes.orEmpty()
                .filter { it.isAnimationStudio == false && !it.name.isNullOrBlank() }
                .map { AniListAnimeDetailsDomainModel.Link(name = it.name.orEmpty(), url = it.siteUrl.orEmpty()) },
            themes = media?.tags.orEmpty()
                .filter { it.isMediaSpoiler != true && !it.name.isNullOrBlank() }
                .take(Constant.MAX_THEMES)
                .map {
                    val name = it.name.orEmpty()
                    AniListAnimeDetailsDomainModel.Link(
                        name = name,
                        url = "${Constant.ANILIST_TAG_URL}${URLEncoder.encode(name, "UTF-8")}"
                    )
                }
        )
    }

    fun mapToMangaDetailsDomainModel(response: AniListDetailsResponse): AniListMangaDetailsDomainModel {
        val media = response.data?.media
        return AniListMangaDetailsDomainModel(
            favorites = media?.favourites.orZero(),
            themes = media?.tags.orEmpty()
                .filter { it.isMediaSpoiler != true && !it.name.isNullOrBlank() }
                .take(Constant.MAX_THEMES)
                .map {
                    val name = it.name.orEmpty()
                    AniListAnimeDetailsDomainModel.Link(
                        name = name,
                        url = "${Constant.ANILIST_MANGA_TAG_URL}${URLEncoder.encode(name, "UTF-8")}"
                    )
                }
        )
    }

    fun mapToCastDomainModel(
        response: AniListCastResponse
    ): List<CastDomainModel> = response.data?.media?.characters?.edges.orEmpty()
        .mapNotNull { edge ->
            val character = edge.node ?: return@mapNotNull null
            CastDomainModel(
                character = CastDomainModel.Character(
                    images = CastDomainModel.Images(
                        jpg = CastDomainModel.Jpg(imageUrl = character.image?.medium.orEmpty())
                    ),
                    id = character.id.orZero(),
                    name = character.name?.full.orEmpty(),
                    url = character.siteUrl.orEmpty()
                ),
                role = edge.role.toRoleLabel(),
                voiceActors = edge.voiceActors.orEmpty().firstOrNull()?.toVoiceActor()
                    ?: CastDomainModel.VoiceActor()
            )
        }

    fun mapToCastPage(response: AniListCastResponse): PagedResult<CastDomainModel> = PagedResult(
        items = mapToCastDomainModel(response),
        hasNextPage = response.data?.media?.characters?.pageInfo?.hasNextPage ?: false
    )

    fun mapToStaffPage(response: AniListStaffResponse): PagedResult<StaffDomainModel> = PagedResult(
        items = mapToStaffDomainModel(response),
        hasNextPage = response.data?.media?.staff?.pageInfo?.hasNextPage ?: false
    )

    fun mapToStaffDomainModel(
        response: AniListStaffResponse
    ): List<StaffDomainModel> = response.data?.media?.staff?.edges.orEmpty()
        .mapNotNull { edge ->
            val person = edge.node ?: return@mapNotNull null
            StaffDomainModel(
                person = StaffDomainModel.Person(
                    images = StaffDomainModel.Images(
                        jpg = StaffDomainModel.Jpg(imageUrl = person.image?.medium.orEmpty())
                    ),
                    id = person.id.orZero(),
                    name = person.name?.full.orEmpty(),
                    url = person.siteUrl.orEmpty()
                ),
                positions = listOfNotNull(edge.role?.takeIf { it.isNotBlank() })
            )
        }

    private fun AniListPerson.toVoiceActor() = CastDomainModel.VoiceActor(
        language = Constant.JAPANESE,
        person = CastDomainModel.Person(
            images = CastDomainModel.PersonImages(
                jpg = CastDomainModel.PersonImagesJpg(imageUrl = image?.medium.orEmpty())
            ),
            id = id.orZero(),
            name = name?.full.orEmpty(),
            url = siteUrl.orEmpty()
        )
    )

    /** AniList roles are upper case (`MAIN`); the UI expects them capitalized (`Main`). */
    private fun String?.toRoleLabel(): String =
        orEmpty().lowercase().replaceFirstChar { it.titlecase() }
}
