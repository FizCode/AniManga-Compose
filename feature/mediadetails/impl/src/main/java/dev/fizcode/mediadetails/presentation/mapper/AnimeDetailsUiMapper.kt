package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.common.util.extensions.airingStatus
import dev.fizcode.common.util.extensions.animeMediaType
import dev.fizcode.common.util.extensions.toCapitalFirstChar
import dev.fizcode.common.util.extensions.toCommaSeparators
import dev.fizcode.common.util.extensions.toCompactNumber
import dev.fizcode.common.util.extensions.toFormattedTime
import dev.fizcode.common.util.extensions.toStringOrNa
import dev.fizcode.common.util.extensions.toStringWithComma
import dev.fizcode.mediadetails.presentation.header.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeDataWithLink
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetails
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetailsInfoUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeInfo
import dev.fizcode.mediadetails.presentation.info.model.AnimeRelatedUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeThemes
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.RelatedAnime
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import dev.fizcode.mediadetails.util.Constant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class AnimeDetailsUiMapper {

    fun mapToAnimeDetailsUiModel(domainModel: AnimeDetailsDomainModel): AnimeDetailsUiModel =
        AnimeDetailsUiModel(
            animeDetailsHeaderUiModel = domainModel.toHeaderUiModel(),
            animeDetailsInfoUiModel = domainModel.toInfoUiModel(),
        )

    private fun AnimeDetailsDomainModel.toHeaderUiModel() = AnimeDetailsHeaderUiModel(
        pictures = malDomainModel.pictures.map { it.medium }.toImmutableList(),
        largePicture = malDomainModel.pictures.map { it.large }.toImmutableList(),
        posterPath = malDomainModel.mainPicture.medium,
        title = malDomainModel.title,
        mediaType = animeMediaType(malDomainModel.mediaType),
        releaseSeason = malDomainModel.startSeason.season.toCapitalFirstChar() +
                " ${malDomainModel.startSeason.year}",
        studio = malDomainModel.studios.toStringWithComma { it.name },
        releaseInfo = episodeInfo(malDomainModel.numEpisodes) +
                ", ${airingStatus(malDomainModel.status)}",
        duration = "${malDomainModel.averageEpisodeDuration.toFormattedTime()} per ep.",
        rank = "#${malDomainModel.rank.toCompactNumber()}",
        popularity = "#${malDomainModel.popularity.toCompactNumber()}",
        members = malDomainModel.numListUsers.toCompactNumber(),
        favorites = (jikanDomainModel?.favorites ?: malDomainModel.numFavorites)
            .toCompactNumber(zeroIsNa = true),
        score = malDomainModel.mean.toStringOrNa(),
        stars = malDomainModel.mean,
        totalVote = malDomainModel.numScoringUsers.toCompactNumber(),
        genre = malDomainModel.genres.map { it.name }.toImmutableList(),
    )

    private fun AnimeDetailsDomainModel.toInfoUiModel(): AnimeDetailsInfoUiModel =
        AnimeDetailsInfoUiModel(
            animeDetails = mapToAnimeDetails(jikan = jikanDomainModel, mal = malDomainModel),
            animeInfo = mapToAnimeInfo(jikan = jikanDomainModel, mal = malDomainModel),
            animeThemes = AnimeThemes(
                openingTheme = (jikanDomainModel?.theme?.openings ?: malDomainModel.openingThemes)
                    .toImmutableList(),
                endingTheme = (jikanDomainModel?.theme?.endings ?: malDomainModel.endingThemes)
                    .toImmutableList()
            ),
            isExtendedInfoAvailable = jikanDomainModel != null
        )

    private fun mapToAnimeDetails(
        jikan: JikanAnimeDetailsDomainModel?,
        mal: MalAnimeDetailsDomainModel
    ) = AnimeDetails(
        synonym = (jikan?.titleSynonyms ?: mal.alternativeTitles.synonyms).toStringWithComma(),
        japanese = jikan?.titleJapanese ?: mal.alternativeTitles.ja,
        english = jikan?.titleEnglish ?: mal.alternativeTitles.en,
        synopsis = mal.synopsis,
        background = mal.background
    )

    private fun mapToAnimeInfo(
        jikan: JikanAnimeDetailsDomainModel?,
        mal: MalAnimeDetailsDomainModel
    ): AnimeInfo {
        val premiered = "${mal.startSeason.season.toCapitalFirstChar()} ${mal.startSeason.year}"
        val duration = "${mal.averageEpisodeDuration.toFormattedTime()} per ep."
        val score = "${mal.mean} (scored by ${mal.numScoringUsers.toCommaSeparators()} users)"
        val ranked = "#${mal.rank.toCommaSeparators(zeroIsNa = true)}"
        val popularity = "#${mal.popularity.toCommaSeparators()}"
        val relatedAnime = mal.relatedAnime.filter {
            it.relationType in setOf("sequel", "prequel", "adaptation")
        }

        return AnimeInfo(
            type = animeMediaType(mal.mediaType),
            episodes = episodeInfo(mal.numEpisodes),
            status = airingStatus(mal.status),
            aired = jikan?.aired?.string ?: airedFromMalDates(mal.startDate, mal.endDate),
            premiered = premiered,
            premieredUrl = "https://google.com",
            producers = jikan?.producers.orEmpty().map {
                AnimeDataWithLink(
                    name = it.name,
                    link = it.url
                )
            }.toImmutableList(),
            licensors = jikan?.licensors.orEmpty().map {
                AnimeDataWithLink(
                    name = it.name,
                    link = it.url
                )
            }.toImmutableList(),
            studios = jikan?.studios?.map {
                AnimeDataWithLink(
                    name = it.name,
                    link = it.url
                )
            }?.toImmutableList() ?: mal.studios.map {
                AnimeDataWithLink(
                    name = it.name,
                    link = "${Constant.MAL_PRODUCER_URL}${it.id}"
                )
            }.toImmutableList(),
            source = AnimeDataWithLink(
                name = jikan?.source ?: mal.source.replace('_', ' ').toCapitalFirstChar(),
                link = "https://google.com"
            ),
            genre = jikan?.genres?.map {
                AnimeDataWithLink(
                    name = it.name,
                    link = it.url
                )
            }?.toImmutableList() ?: mal.genres.map {
                AnimeDataWithLink(
                    name = it.name,
                    link = "${Constant.MAL_GENRE_URL}${it.id}"
                )
            }.toImmutableList(),
            themes = jikan?.themes.orEmpty().map {
                AnimeDataWithLink(
                    name = it.name,
                    link = it.url
                )
            }.toImmutableList(),
            duration = duration,
            rating = jikan?.rating ?: malRatingLabel(mal.rating),
            score = score,
            ranked = ranked,
            popularity = popularity,
            members = (jikan?.members ?: mal.numListUsers).toCommaSeparators(),
            favorites = (jikan?.favorites ?: mal.numFavorites).toCommaSeparators(zeroIsNa = true),
            relatedAnime = mapToRelatedAnime(relatedAnime = relatedAnime)
        )
    }

    private fun mapToRelatedAnime(
        relatedAnime: List<RelatedAnime>
    ): ImmutableList<AnimeRelatedUiModel> = relatedAnime.map {
        AnimeRelatedUiModel(
            relatedId = it.node.id,
            name = it.node.title,
            url = it.node.mainPicture.medium,
            relationType = it.relationTypeFormatted
        )
    }.toImmutableList()


    internal fun mapToAnimeCastUiModel(
        voiceActors: List<JikanCastDomainModel>
    ): ImmutableList<AnimeCastUiModel> = voiceActors.sortedWith(
        compareBy(
            { it.role.lowercase() != Constant.SORT_MAIN },
            { it.character.malId }
        )
    ).take(10).map {
        AnimeCastUiModel(
            characterId = it.character.malId,
            character = it.character.name,
            characterImage = it.character.images.jpg.imageUrl,
            characterRole = it.role,
            characterUrl = it.character.url,
            voiceActorId = it.voiceActors.person.malId,
            voiceActorName = it.voiceActors.person.name,
            voiceActorImage = it.voiceActors.person.images.jpg.imageUrl,
            voiceActorLang = it.voiceActors.language,
            voiceActorUrl = it.voiceActors.person.url
        )
    }.toImmutableList()

    internal fun mapToStaffUiModel(
        jiaknStaffData: List<JikanStaffDomainModel>
    ): ImmutableList<AnimeStaffUiModel> = jiaknStaffData.map {
        AnimeStaffUiModel(
            staffId = it.person.malId,
            name = it.person.name,
            role = it.positions.toStringWithComma(),
            image = it.person.images.jpg.imageUrl,
            url = it.person.url
        )
    }.take(10).toImmutableList()

    /** Formats MAL's `yyyy-MM-dd` dates (month and day may be missing) like Jikan's aired text. */
    private fun airedFromMalDates(startDate: String, endDate: String): String {
        val start = malDateLabel(startDate)
        val end = malDateLabel(endDate)
        return when {
            start.isEmpty() -> Constant.NOT_AVAILABLE
            end.isEmpty() -> "$start to ?"
            start == end -> start
            else -> "$start to $end"
        }
    }

    private fun malDateLabel(date: String): String {
        val parts = date.split("-")
        val year = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: return ""
        val month = parts.getOrNull(1)?.toIntOrNull()?.let { MONTHS.getOrNull(it - 1) }
            ?: return year
        val day = parts.getOrNull(2)?.toIntOrNull() ?: return "$month $year"
        return "$month $day, $year"
    }

    private fun malRatingLabel(rating: String): String = MAL_RATINGS[rating] ?: rating

    private fun episodeInfo(numEpisodes: Int): String = when (numEpisodes) {
        0 -> Constant.UNKNOWN_EPISODE
        1 -> "${numEpisodes.toCommaSeparators()} ${Constant.EPISODE}"
        else -> "${numEpisodes.toCommaSeparators()} ${Constant.EPISODES}"
    }

    private companion object {
        val MONTHS = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        val MAL_RATINGS = mapOf(
            "g" to "G - All Ages",
            "pg" to "PG - Children",
            "pg_13" to "PG-13 - Teens 13 or older",
            "r" to "R - 17+ (violence & profanity)",
            "r+" to "R+ - Mild Nudity",
            "rx" to "Rx - Hentai"
        )
    }
}
