package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.common.util.extensions.orZero
import dev.fizcode.mediadetails.data.response.AniListCastResponse
import dev.fizcode.mediadetails.data.response.AniListPerson
import dev.fizcode.mediadetails.data.response.AniListStaffResponse
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.util.Constant

/**
 * Maps AniList responses to the cast and staff domain models shared with Jikan.
 *
 * The ids in the resulting models are AniList ids, not MyAnimeList ids, and the urls point to
 * AniList.
 */
internal class AniListDomainMapper {

    fun mapToCastDomainModel(
        response: AniListCastResponse
    ): List<JikanCastDomainModel> = response.data?.media?.characters?.edges.orEmpty()
        .mapNotNull { edge ->
            val character = edge.node ?: return@mapNotNull null
            JikanCastDomainModel(
                character = JikanCastDomainModel.Character(
                    images = JikanCastDomainModel.Images(
                        jpg = JikanCastDomainModel.Jpg(imageUrl = character.image?.medium.orEmpty())
                    ),
                    malId = character.id.orZero(),
                    name = character.name?.full.orEmpty(),
                    url = character.siteUrl.orEmpty()
                ),
                role = edge.role.toRoleLabel(),
                voiceActors = edge.voiceActors.orEmpty().firstOrNull()?.toVoiceActor()
                    ?: JikanCastDomainModel.VoiceActor()
            )
        }

    fun mapToStaffDomainModel(
        response: AniListStaffResponse
    ): List<JikanStaffDomainModel> = response.data?.media?.staff?.edges.orEmpty()
        .mapNotNull { edge ->
            val person = edge.node ?: return@mapNotNull null
            JikanStaffDomainModel(
                person = JikanStaffDomainModel.Person(
                    images = JikanStaffDomainModel.Images(
                        jpg = JikanStaffDomainModel.Jpg(imageUrl = person.image?.medium.orEmpty())
                    ),
                    malId = person.id.orZero(),
                    name = person.name?.full.orEmpty(),
                    url = person.siteUrl.orEmpty()
                ),
                positions = listOfNotNull(edge.role?.takeIf { it.isNotBlank() })
            )
        }

    private fun AniListPerson.toVoiceActor() = JikanCastDomainModel.VoiceActor(
        language = Constant.JAPANESE,
        person = JikanCastDomainModel.Person(
            images = JikanCastDomainModel.PersonImages(
                jpg = JikanCastDomainModel.PersonImagesJpg(imageUrl = image?.medium.orEmpty())
            ),
            malId = id.orZero(),
            name = name?.full.orEmpty(),
            url = siteUrl.orEmpty()
        )
    )

    /** AniList roles are upper case (`MAIN`); Jikan's are capitalized (`Main`). */
    private fun String?.toRoleLabel(): String =
        orEmpty().lowercase().replaceFirstChar { it.titlecase() }
}
