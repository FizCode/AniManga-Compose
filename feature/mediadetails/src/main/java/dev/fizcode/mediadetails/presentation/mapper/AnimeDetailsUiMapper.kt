package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.common.util.extensions.airingStatus
import dev.fizcode.common.util.extensions.animeMediaType
import dev.fizcode.common.util.extensions.toCapitalFirstChar
import dev.fizcode.common.util.extensions.toCommaSeparators
import dev.fizcode.common.util.extensions.toCompactNumber
import dev.fizcode.common.util.extensions.toFormattedTime
import dev.fizcode.common.util.extensions.toStringOrNa
import dev.fizcode.common.util.extensions.toStringWithComma
import dev.fizcode.mediadetailheader.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetailinfo.model.AnimeCastUiModel
import dev.fizcode.mediadetailinfo.model.AnimeDataWithLink
import dev.fizcode.mediadetailinfo.model.AnimeDetails
import dev.fizcode.mediadetailinfo.model.AnimeDetailsInfoUiModel
import dev.fizcode.mediadetailinfo.model.AnimeInfo
import dev.fizcode.mediadetailinfo.model.AnimeRelatedUiModel
import dev.fizcode.mediadetailinfo.model.AnimeStaffUiModel
import dev.fizcode.mediadetailinfo.model.AnimeThemes
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
        favorites = jikanDomainModel.favorites.toCompactNumber(),
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
                openingTheme = jikanDomainModel.theme.openings.toImmutableList(),
                endingTheme = jikanDomainModel.theme.endings.toImmutableList()
            )
        )

    private fun mapToAnimeDetails(
        jikan: JikanAnimeDetailsDomainModel,
        mal: MalAnimeDetailsDomainModel
    ) = AnimeDetails(
        synonym = jikan.titleSynonyms.toStringWithComma(),
        japanese = jikan.titleJapanese,
        english = jikan.titleEnglish,
        synopsis = mal.synopsis,
        background = mal.background
    )

    private fun mapToAnimeInfo(
        jikan: JikanAnimeDetailsDomainModel,
        mal: MalAnimeDetailsDomainModel
    ) = AnimeInfo(
        type = animeMediaType(mal.mediaType),
        episodes = episodeInfo(mal.numEpisodes),
        status = airingStatus(mal.status),
        aired = jikan.aired.string,
        premiered = "${mal.startSeason.season.toCapitalFirstChar()} ${mal.startSeason.year}",
        premieredUrl = "https://google.com",
        producers = jikan.producers.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url
            )
        }.toImmutableList(),
        licensors = jikan.licensors.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url
            )
        }.toImmutableList(),
        studios = jikan.studios.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url
            )
        }.toImmutableList(),
        source = AnimeDataWithLink(name = jikan.source, link = "https://google.com"),
        genre = jikan.genres.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url
            )
        }.toImmutableList(),
        themes = jikan.themes.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url
            )
        }.toImmutableList(),
        duration = "${mal.averageEpisodeDuration.toFormattedTime()} per ep.",
        rating = jikan.rating,
        score = "${mal.mean} (scored by ${mal.numScoringUsers.toCommaSeparators()} users)",
        ranked = "#${mal.rank.toCommaSeparators(zeroIsNa = true)}",
        popularity = "#${mal.popularity.toCommaSeparators()}",
        members = jikan.members.toCommaSeparators(),
        favorites = jikan.favorites.toCommaSeparators(),
        relatedAnime = mapToRelatedAnime(mal.relatedAnime)
    )

    private fun mapToRelatedAnime(
        relatedAnime: List<RelatedAnime>
    ): ImmutableList<AnimeRelatedUiModel> = relatedAnime.map {
        AnimeRelatedUiModel(
            relatedId = it.node.id,
            name = it.node.title,
            image = it.node.mainPicture.medium,
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

    private fun episodeInfo(numEpisodes: Int): String = when (numEpisodes) {
        0 -> Constant.UNKNOWN_EPISODE
        1 -> "${numEpisodes.toCommaSeparators()} ${Constant.EPISODE}"
        else -> "${numEpisodes.toCommaSeparators()} ${Constant.EPISODES}"
    }
}
