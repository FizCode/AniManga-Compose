package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.common.util.extensions.toCapitalFirstChar
import dev.fizcode.common.util.extensions.toCommaSeparators
import dev.fizcode.common.util.extensions.toCompactNumber
import dev.fizcode.common.util.extensions.toStringOrNa
import dev.fizcode.common.util.extensions.toStringWithComma
import dev.fizcode.mediadetails.domain.model.AniListMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.CastDomainModel
import dev.fizcode.mediadetails.domain.model.MalMangaDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.MangaDetailsDomainModel
import dev.fizcode.mediadetails.presentation.header.model.MangaDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeDataWithLink
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetails
import dev.fizcode.mediadetails.presentation.info.model.AnimeRelatedUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaDetailsInfoUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaInfo
import dev.fizcode.mediadetails.presentation.model.MangaDetailsUiModel
import dev.fizcode.mediadetails.util.Constant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import dev.fizcode.common.settings.MediaTitles

internal class MangaDetailsUiMapper {

    fun mapToMangaDetailsUiModel(domainModel: MangaDetailsDomainModel): MangaDetailsUiModel =
        MangaDetailsUiModel(
            mangaDetailsHeaderUiModel = domainModel.toHeaderUiModel(),
            mangaDetailsInfoUiModel = domainModel.toInfoUiModel()
        )

    private fun MangaDetailsDomainModel.toHeaderUiModel(): MangaDetailsHeaderUiModel {
        val mal = malDomainModel
        return MangaDetailsHeaderUiModel(
            pictures = mal.pictures.map { it.medium }.toImmutableList(),
            largePicture = mal.pictures.map { it.large }.toImmutableList(),
            posterPath = mal.mainPicture.medium,
            title = mal.title,
            titles = MediaTitles(mal.title, mal.alternativeTitles.toCommon()),
            mediaType = mangaMediaTypeLabel(mal.mediaType),
            serialization = mal.serialization.toStringWithComma { it.name },
            publishInfo = "${volumeAndChapter(mal)} | ${mangaStatusLabel(mal.status)}",
            publishedDates = MalDateFormatter.range(mal.startDate, mal.endDate),
            rank = "#${mal.rank.toCompactNumber()}",
            popularity = "#${mal.popularity.toCompactNumber()}",
            members = mal.numListUsers.toCompactNumber(),
            favorites = (aniListDomainModel?.favorites ?: 0).toCompactNumber(zeroIsNa = true),
            score = mal.mean.toStringOrNa(),
            stars = mal.mean,
            totalVote = mal.numScoringUsers.toCompactNumber(),
            genre = mal.genres.map { it.name }.toImmutableList()
        )
    }

    private fun MangaDetailsDomainModel.toInfoUiModel() = MangaDetailsInfoUiModel(
        mangaDetails = AnimeDetails(
            synonym = malDomainModel.alternativeTitles.synonyms.toStringWithComma(),
            japanese = malDomainModel.alternativeTitles.ja,
            english = malDomainModel.alternativeTitles.en,
            synopsis = malDomainModel.synopsis,
            background = malDomainModel.background
        ),
        mangaInfo = mapToMangaInfo(mal = malDomainModel, aniList = aniListDomainModel),
        isExtendedInfoAvailable = aniListDomainModel != null
    )

    private fun mapToMangaInfo(
        mal: MalMangaDetailsDomainModel,
        aniList: AniListMangaDetailsDomainModel?
    ) = MangaInfo(
        type = mangaMediaTypeLabel(mal.mediaType),
        volumes = countLabel(mal.numVolumes),
        chapters = countLabel(mal.numChapters),
        status = mangaStatusLabel(mal.status),
        published = MalDateFormatter.range(mal.startDate, mal.endDate),
        authors = mal.authors.map {
            AnimeDataWithLink(name = it.name, link = "${Constant.MAL_PEOPLE_URL}${it.id}")
        }.toImmutableList(),
        serialization = mal.serialization.map {
            AnimeDataWithLink(name = it.name, link = "${Constant.MAL_MANGA_MAGAZINE_URL}${it.id}")
        }.toImmutableList(),
        genre = mal.genres.map {
            AnimeDataWithLink(name = it.name, link = "${Constant.MAL_MANGA_GENRE_URL}${it.id}")
        }.toImmutableList(),
        themes = aniList?.themes.orEmpty().map {
            AnimeDataWithLink(name = it.name, link = it.url)
        }.toImmutableList(),
        score = "${mal.mean} (scored by ${mal.numScoringUsers.toCommaSeparators()} users)",
        ranked = "#${mal.rank.toCommaSeparators(zeroIsNa = true)}",
        popularity = "#${mal.popularity.toCommaSeparators()}",
        members = mal.numListUsers.toCommaSeparators(),
        favorites = (aniList?.favorites ?: 0).toCommaSeparators(zeroIsNa = true),
        relatedManga = mal.relatedManga.map {
            AnimeRelatedUiModel(
                relatedId = it.node.id,
                name = it.node.title,
                url = it.node.mainPicture.medium,
                relationType = it.relationTypeFormatted
            )
        }.toImmutableList()
    )

    internal fun mapToCharacterUiModel(
        characters: List<CastDomainModel>
    ): ImmutableList<MangaCharacterUiModel> = characters.sortedWith(
        compareBy({ it.role.lowercase() != Constant.SORT_MAIN }, { it.character.id })
    ).take(MAX_CHARACTERS).map { it.toCharacterUiModel() }.toImmutableList()

    /** A page of the full character list, in the order AniList sends it. */
    internal fun mapToCharacterPage(
        characters: List<CastDomainModel>
    ): ImmutableList<MangaCharacterUiModel> =
        characters.map { it.toCharacterUiModel() }.toImmutableList()

    private fun CastDomainModel.toCharacterUiModel() = MangaCharacterUiModel(
        characterId = character.id,
        name = character.name,
        image = character.images.jpg.imageUrl,
        role = role,
        url = character.url
    )

    private fun volumeAndChapter(mal: MalMangaDetailsDomainModel) =
        "${Constant.VOLUME} ${countOrUnknown(mal.numVolumes)} " +
                "${Constant.CHAPTER} ${countOrUnknown(mal.numChapters)}"

    /** MAL reports 0 while the total is not known yet. */
    private fun countOrUnknown(value: Int) = if (value == 0) Constant.UNKNOWN_COUNT else value.toString()

    private fun countLabel(value: Int) = if (value == 0) Constant.UNKNOWN else value.toCommaSeparators()

    private fun mangaMediaTypeLabel(mediaType: String) = when (mediaType) {
        "manga" -> "Manga"
        "novel" -> "Novel"
        "light_novel" -> "Light Novel"
        "one_shot" -> "One-Shot"
        "doujinshi" -> "Doujinshi"
        "manhwa" -> "Manhwa"
        "manhua" -> "Manhua"
        "oel" -> "OEL"
        else -> mediaType.replace('_', ' ').toCapitalFirstChar().ifBlank { Constant.UNKNOWN }
    }

    private fun mangaStatusLabel(status: String) = when (status) {
        "finished" -> "Finished"
        "currently_publishing" -> "Publishing"
        "not_yet_published" -> "Not Yet Published"
        "on_hiatus" -> "On Hiatus"
        "discontinued" -> "Discontinued"
        else -> Constant.UNKNOWN
    }

    private companion object {
        const val MAX_CHARACTERS = 20
    }
}
