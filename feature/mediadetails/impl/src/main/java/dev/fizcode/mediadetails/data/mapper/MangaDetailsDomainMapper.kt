package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.common.util.extensions.orZero
import dev.fizcode.mediadetails.data.response.MalAnimeDetailsResponse
import dev.fizcode.mediadetails.data.response.MalMangaDetailsResponse
import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.Genre
import dev.fizcode.mediadetails.domain.model.MainPicture
import dev.fizcode.mediadetails.domain.model.MalMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.Node
import dev.fizcode.mediadetails.domain.model.Picture
import dev.fizcode.mediadetails.domain.model.RelatedAnime

internal class MangaDetailsDomainMapper {

    fun mapToMalMangaDetailsModel(response: MalMangaDetailsResponse): MalMangaDetailsDomainModel =
        MalMangaDetailsDomainModel(
            alternativeTitles = response.alternativeTitles?.let {
                AlternativeTitles(
                    en = it.en.orEmpty(),
                    ja = it.ja.orEmpty(),
                    synonyms = it.synonyms.orEmpty()
                )
            } ?: AlternativeTitles(),
            authors = response.authors.orEmpty().mapNotNull { author ->
                val node = author.node ?: return@mapNotNull null
                val name = listOfNotNull(node.firstName, node.lastName)
                    .filter { it.isNotBlank() }
                    .joinToString(" ")
                if (name.isEmpty()) return@mapNotNull null
                MalMangaDetailsDomainModel.Author(
                    id = node.id.orZero(),
                    name = name,
                    role = author.role.orEmpty()
                )
            },
            background = response.background.orEmpty(),
            endDate = response.endDate.orEmpty(),
            genres = response.genres.orEmpty().map {
                Genre(id = it.id.orZero(), name = it.name.orEmpty())
            },
            id = response.id.orZero(),
            mainPicture = response.mainPicture.toDomainModel(),
            mean = response.mean.orZero(),
            mediaType = response.mediaType.orEmpty(),
            numChapters = response.numChapters.orZero(),
            numListUsers = response.numListUsers.orZero(),
            numScoringUsers = response.numScoringUsers.orZero(),
            numVolumes = response.numVolumes.orZero(),
            pictures = response.pictures.orEmpty().map {
                Picture(large = it.large.orEmpty(), medium = it.medium.orEmpty())
            },
            popularity = response.popularity.orZero(),
            rank = response.rank.orZero(),
            relatedManga = response.relatedManga.orEmpty().map {
                RelatedAnime(
                    node = Node(
                        id = it.node?.id.orZero(),
                        mainPicture = it.node?.mainPicture.toDomainModel(),
                        title = it.node?.title.orEmpty()
                    ),
                    relationType = it.relationType.orEmpty(),
                    relationTypeFormatted = it.relationTypeFormatted.orEmpty()
                )
            },
            serialization = response.serialization.orEmpty().mapNotNull {
                val node = it.node ?: return@mapNotNull null
                if (node.name.isNullOrBlank()) return@mapNotNull null
                MalMangaDetailsDomainModel.Serialization(id = node.id.orZero(), name = node.name)
            },
            startDate = response.startDate.orEmpty(),
            status = response.status.orEmpty(),
            synopsis = response.synopsis.orEmpty(),
            title = response.title.orEmpty()
        )

    private fun MalAnimeDetailsResponse.MainPicture?.toDomainModel() = this?.let {
        MainPicture(large = it.large.orEmpty(), medium = it.medium.orEmpty())
    } ?: MainPicture()
}
