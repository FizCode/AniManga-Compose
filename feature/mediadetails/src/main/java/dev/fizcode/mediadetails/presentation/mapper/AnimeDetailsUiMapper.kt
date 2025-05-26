package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.common.util.airingStatus
import dev.fizcode.common.util.animeMediaType
import dev.fizcode.common.util.toCapitalFirstChar
import dev.fizcode.common.util.toCommaSeparators
import dev.fizcode.common.util.toFormattedTime
import dev.fizcode.mediadetailheader.model.AnimeDetailsHeaderUiModel
import dev.fizcode.mediadetailinfo.model.AnimeCast
import dev.fizcode.mediadetailinfo.model.AnimeCharacters
import dev.fizcode.mediadetailinfo.model.AnimeDataWithLink
import dev.fizcode.mediadetailinfo.model.AnimeDetails
import dev.fizcode.mediadetailinfo.model.AnimeDetailsInfoUiModel
import dev.fizcode.mediadetailinfo.model.AnimeInfo
import dev.fizcode.mediadetailinfo.model.AnimeStaff
import dev.fizcode.mediadetailinfo.model.AnimeThemes
import dev.fizcode.mediadetails.domain.model.AnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanData
import dev.fizcode.mediadetails.domain.model.JikanStaffData
import dev.fizcode.mediadetails.domain.model.JikanVAData
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.presentation.model.AnimeDetailsUiModel
import dev.fizcode.mediadetails.util.Constant

internal class AnimeDetailsUiMapper {

    fun mapToAnimeDetailsUiModel(domainModel: AnimeDetailsDomainModel): AnimeDetailsUiModel =
        AnimeDetailsUiModel(
            animeDetailsHeaderUiModel = domainModel.toHeaderUiModel(),
            animeDetailsInfoUiModel = domainModel.toInfoUiModel(),
        )

    private fun AnimeDetailsDomainModel.toHeaderUiModel() = AnimeDetailsHeaderUiModel(
        pictures = malDomainModel.pictures.map
        { it.medium },
        posterPath = malDomainModel.mainPicture.medium,
        title = malDomainModel.title,
        mediaType = animeMediaType(malDomainModel.mediaType),
        releaseSeason = malDomainModel.startSeason.season.toCapitalFirstChar() +
                " ${malDomainModel.startSeason.year}",
        studio = malDomainModel.studios.joinToString(", ")
        { it.name },
        releaseInfo = episodeInfo(malDomainModel.numEpisodes) +
                ", ${airingStatus(malDomainModel.status)}",
        duration = "${malDomainModel.averageEpisodeDuration.toFormattedTime()} per ep.",
        rank = malDomainModel.rank,
        popularity = malDomainModel.popularity,
        members = malDomainModel.numListUsers,
        favorites = jikanDomainModel.data.favorites,
        score = malDomainModel.mean,
        totalVote = malDomainModel.numScoringUsers,
        genre = malDomainModel.genres.map { it.name },
    )

    private fun AnimeDetailsDomainModel.toInfoUiModel(): AnimeDetailsInfoUiModel {

        val jikanData = jikanDomainModel.data

        return AnimeDetailsInfoUiModel(
            animeDetails = mapToAnimeDetails(jikan = jikanData, mal = malDomainModel),
            animeInfo = mapToAnimeInfo(jikan = jikanData, mal = malDomainModel),
            animeCast = mapToAnimeCast(
                jikanVaData = jikanDomainModel.voiceActors,
                jikanStaffs = jikanDomainModel.staffs
            ),
            animeThemes = AnimeThemes(
                openingTheme = jikanData.theme.openings,
                endingTheme = jikanData.theme.endings
            )
        )
    }

    private fun mapToAnimeDetails(
        jikan: JikanData,
        mal: MalAnimeDetailsDomainModel
    ) = AnimeDetails(
        synonym = jikan.titleSynonyms.joinToString(", "),
        japanese = jikan.titleJapanese,
        english = jikan.titleEnglish,
        synopsis = mal.synopsis,
        background = mal.background
    )

    private fun mapToAnimeInfo(
        jikan: JikanData,
        mal: MalAnimeDetailsDomainModel
    ) = AnimeInfo(
        type = animeMediaType(mal.mediaType),
        episodes = episodeInfo(mal.numEpisodes),
        status = airingStatus(mal.status),
        aired = jikan.aired.string,
        premiered = "${mal.startSeason.season.toCapitalFirstChar()} ${mal.startSeason.year}",
        premieredUrl = "https://google.com", // TODO
        producers = jikan.producers.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url // TODO
            )
        },
        licensors = jikan.licensors.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url // TODO
            )
        },
        studios = jikan.studios.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url // TODO
            )
        },
        source = AnimeDataWithLink(name = jikan.source, link = "https://google.com"), // TODO
        genre = jikan.genres.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url // TODO
            )
        },
        themes = jikan.themes.map {
            AnimeDataWithLink(
                name = it.name,
                link = it.url // TODO
            )
        },
        duration = "${mal.averageEpisodeDuration.toFormattedTime()} per ep.",
        rating = jikan.rating,
        score = "${mal.mean} (scored by ${mal.numScoringUsers.toCommaSeparators()} users)",
        ranked = "#${mal.rank.toCommaSeparators()}",
        popularity = "#${mal.popularity.toCommaSeparators()}",
        members = jikan.members.toCommaSeparators(),
        favorites = jikan.favorites.toCommaSeparators()
    )

    private fun mapToAnimeCast(
        jikanVaData: List<JikanVAData>,
        jikanStaffs: List<JikanStaffData>
    ): AnimeCast = AnimeCast(
        characters = mapToCharacterUiModel(jikanVaData.take(10)),
        animeStaffs = mapToStaffUiModel(jikanStaffs).take(10),
    )

    private fun mapToCharacterUiModel(voiceActors: List<JikanVAData>): List<AnimeCharacters> =
        voiceActors.map {
            AnimeCharacters(
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
        }

    private fun mapToStaffUiModel(staff: List<JikanStaffData>): List<AnimeStaff> =
        staff.map {
            AnimeStaff(
                staffId = it.person.malId,
                name = it.person.name,
                role = it.positions.joinToString(", "),
                image = it.person.images.jpg.imageUrl,
                url = it.person.url
            )
        }

    private fun episodeInfo(numEpisodes: Int): String = when (numEpisodes) {
        0 -> Constant.UNKNOWN_EPISODE
        1 -> "${numEpisodes.toCommaSeparators()} ${Constant.EPISODE}"
        else -> "${numEpisodes.toCommaSeparators()} ${Constant.EPISODES}"
    }
}
