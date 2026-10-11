package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.common.util.extensions.orDash
import dev.fizcode.common.util.extensions.orDashList
import dev.fizcode.common.util.extensions.orZero
import dev.fizcode.mediadetails.data.response.MalAnimeDetailsResponse
import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.Broadcast
import dev.fizcode.mediadetails.domain.model.Genre
import dev.fizcode.mediadetails.domain.model.MainPicture
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.Node
import dev.fizcode.mediadetails.domain.model.Picture
import dev.fizcode.mediadetails.domain.model.RelatedAnime
import dev.fizcode.mediadetails.domain.model.StartSeason
import dev.fizcode.mediadetails.domain.model.Studio
import dev.fizcode.mediadetails.util.Constant

internal class AnimeDetailsDomainMapper {

    fun mapToMalAnimeDetailsModel(response: MalAnimeDetailsResponse): MalAnimeDetailsDomainModel =
        MalAnimeDetailsDomainModel(
            alternativeTitles = response.alternativeTitles.toDomainModel(),
            averageEpisodeDuration = response.averageEpisodeDuration.orZero(),
            background = response.background.orEmpty(),
            broadcast = response.broadcast.toDomainModel(),
            endDate = response.endDate.orEmpty(),
            endingThemes = response.endingThemes.toThemeTexts(),
            genres = response.genres.toGenreDomainModel(),
            id = response.id.orZero(),
            mainPicture = response.mainPicture.toDomainModel(),
            mean = response.mean.orZero(),
            mediaType = response.mediaType.orEmpty(),
            numEpisodes = response.numEpisodes.orZero(),
            numFavorites = response.numFavorites.orZero(),
            numListUsers = response.numListUsers.orZero(),
            numScoringUsers = response.numScoringUsers.orZero(),
            openingThemes = response.openingThemes.toThemeTexts(),
            pictures = response.pictures.toPictureDomainModel(),
            popularity = response.popularity.orZero(),
            rank = response.rank.orZero(),
            rating = response.rating.orEmpty(),
            relatedAnime = response.relatedAnime.toRelatedDomainModel(),
            source = response.source.orDash(),
            startDate = response.startDate.orEmpty(),
            startSeason = response.startSeason.toDomainModel(),
            status = response.status.orEmpty(),
            studios = response.studios.toStudioDomainModel(),
            synopsis = response.synopsis.orEmpty(),
            title = response.title.orEmpty()
        )

    private fun MalAnimeDetailsResponse.AlternativeTitles?.toDomainModel() = this?.run {
        AlternativeTitles(
            en = en.orEmpty(),
            ja = ja.orEmpty(),
            synonyms = synonyms?.map { it }.orEmpty()
        )
    } ?: AlternativeTitles()

    private fun MalAnimeDetailsResponse.Broadcast?.toDomainModel() = this?.run {
        Broadcast(
            dayOfTheWeek = dayOfTheWeek.orEmpty(),
            startTime = startTime.orEmpty()
        )
    } ?: Broadcast()

    private fun List<MalAnimeDetailsResponse.Genre>?.toGenreDomainModel() = this.orEmpty().map {
        Genre(
            id = it.id.orZero(),
            name = it.name.orDash()
        )
    }

    private fun MalAnimeDetailsResponse.MainPicture?.toDomainModel() = this?.run {
        MainPicture(
            large = large.orEmpty(),
            medium = medium.orEmpty()
        )
    } ?: MainPicture()

    private fun List<MalAnimeDetailsResponse.Theme>?.toThemeTexts() =
        this.orEmpty().mapNotNull { it.text?.takeIf(String::isNotBlank) }

    private fun List<MalAnimeDetailsResponse.Picture>?.toPictureDomainModel() = this.orEmpty().map {
        Picture(
            large = it.large.orEmpty(),
            medium = it.medium.orEmpty()
        )
    }

    private fun List<MalAnimeDetailsResponse.RelatedAnime>?.toRelatedDomainModel() =
        this.orEmpty().map {
            RelatedAnime(
                node = it.node.toDomainModel(),
                relationType = it.relationType.orEmpty(),
                relationTypeFormatted = it.relationTypeFormatted.orEmpty()
            )
        }

    private fun MalAnimeDetailsResponse.Node?.toDomainModel() = this?.run {
        Node(
            id = id.orZero(),
            mainPicture = mainPicture.toDomainModel(),
            title = title.orEmpty()
        )
    } ?: Node()

    private fun MalAnimeDetailsResponse.StartSeason?.toDomainModel() = this?.run {
        StartSeason(
            season = season.orEmpty(),
            year = year.orZero()
        )
    } ?: StartSeason()

    private fun List<MalAnimeDetailsResponse.Studio>?.toStudioDomainModel() = this.orEmpty().map {
        Studio(
            id = it.id.orZero(),
            name = it.name.orDash()
        )
    }
}
