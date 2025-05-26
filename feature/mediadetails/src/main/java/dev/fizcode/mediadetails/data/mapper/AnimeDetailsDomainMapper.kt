package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.common.util.orDash
import dev.fizcode.common.util.orDashList
import dev.fizcode.common.util.orZero
import dev.fizcode.mediadetails.data.response.JikanAnimeDetailsResponse
import dev.fizcode.mediadetails.data.response.JikanStaffResponse
import dev.fizcode.mediadetails.data.response.JikanVoiceActorsResponse
import dev.fizcode.mediadetails.data.response.MalAnimeDetailsResponse
import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.Broadcast
import dev.fizcode.mediadetails.domain.model.Genre
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanData
import dev.fizcode.mediadetails.domain.model.JikanStaffData
import dev.fizcode.mediadetails.domain.model.JikanVAData
import dev.fizcode.mediadetails.domain.model.MainPicture
import dev.fizcode.mediadetails.domain.model.MalAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.Picture
import dev.fizcode.mediadetails.domain.model.StartSeason
import dev.fizcode.mediadetails.domain.model.Studio
import dev.fizcode.mediadetails.util.Constant

internal class AnimeDetailsDomainMapper {

    // MAL Anime Domain Mapper section
    fun mapToMalAnimeDetailsModel(response: MalAnimeDetailsResponse): MalAnimeDetailsDomainModel =
        MalAnimeDetailsDomainModel(
            alternativeTitles = response.alternativeTitles.toDomainModel(),
            averageEpisodeDuration = response.averageEpisodeDuration.orZero(),
            background = response.background.orEmpty(),
            broadcast = response.broadcast.toDomainModel(),
            endDate = response.endDate.orEmpty(),
            genres = response.genres.toGenreDomainModel(),
            id = response.id.orZero(),
            mainPicture = response.mainPicture.toDomainModel(),
            mean = response.mean.orZero(),
            mediaType = response.mediaType.orEmpty(),
            numEpisodes = response.numEpisodes.orZero(),
            numListUsers = response.numListUsers.orZero(),
            numScoringUsers = response.numScoringUsers.orZero(),
            pictures = response.pictures.toPictureDomainModel(),
            popularity = response.popularity.orZero(),
            rank = response.rank.orZero(),
            rating = response.rating.orEmpty(),
            source = response.source.orDash(),
            startDate = response.startDate.orEmpty(),
            startSeason = response.startSeason.toDomainModel(),
            status = response.status.orEmpty(),
            studios = response.studios.toStudioDomainModel(),
            synopsis = response.synopsis.orEmpty(),
            title = response.title.orEmpty()
        )

    private fun MalAnimeDetailsResponse.AlternativeTitles?.toDomainModel() = this?.run {
        return AlternativeTitles(
            en = en.orEmpty(),
            ja = ja.orEmpty(),
            synonyms = synonyms?.map { it }.orEmpty()
        )
    } ?: AlternativeTitles()

    private fun MalAnimeDetailsResponse.Broadcast?.toDomainModel() = this?.run {
        return Broadcast(
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

    private fun List<MalAnimeDetailsResponse.Picture>?.toPictureDomainModel() = this.orEmpty().map {
        Picture(
            large = it.large.orEmpty(),
            medium = it.medium.orEmpty()
        )
    }

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

    // Jikan Anime Domain Mapper section
    fun mapToJikanAnimeDetailsModel(
        detailsResponse: JikanAnimeDetailsResponse,
        castResponse: JikanVoiceActorsResponse,
        staffResponse: JikanStaffResponse
    ): JikanAnimeDetailsDomainModel =
        JikanAnimeDetailsDomainModel(
            data = detailsResponse.data.mapToJikanData(),
            voiceActors = castResponse.data.toCastDomainModel(),
            staffs = staffResponse.data.toStaffDomainModel()
        )

    private fun JikanAnimeDetailsResponse.Data?.mapToJikanData() = this?.run {
        JikanData(
            aired = aired.toDomainModel(),
            airing = airing ?: false,
            approved = approved ?: false,
            background = background.orEmpty(),
            broadcast = broadcast.toDomainModel(),
            demographics = demographics.toJikanDemographicDomainModel(),
            duration = duration.orEmpty(),
            episodes = episodes.orZero(),
            explicitGenres = explicitGenres.toJikanExplicitGenresDomainModel(),
            favorites = favorites.orZero(),
            genres = genres.toJikanGenreDomainModel(),
            images = images.toDomainModel(),
            licensors = licensors.toJikanLicensorDomainModel(),
            malId = malId.orZero(),
            members = members.orZero(),
            popularity = popularity.orZero(),
            producers = producers.toJikanProducerDomainModel(),
            rank = rank.orZero(),
            rating = rating.orEmpty(),
            score = score.orZero(),
            scoredBy = scoredBy.orZero(),
            season = season.orEmpty(),
            source = source.orEmpty(),
            status = status.orEmpty(),
            studios = studios.toJikanStudioDomainModel(),
            synopsis = synopsis.orEmpty(),
            theme = theme.mapToDomainModel(),
            themes = themes.toJikanThemeDomainModel(),
            title = title.orDash(),
            titleEnglish = titleEnglish.orDash(),
            titleJapanese = titleJapanese.orDash(),
            titleSynonyms = titleSynonyms.orDashList(),
            titles = titles.toJikanTitleDomainModel(),
            trailer = trailer.toDomainModel(),
            type = type.orEmpty(),
            url = url.orEmpty(),
            year = year.orZero()
        )
    } ?: JikanData()

    private fun JikanAnimeDetailsResponse.Aired?.toDomainModel() = this?.run {
        JikanData.Aired(
            from = from.orEmpty(),
            string = string.orEmpty(),
            to = to.orEmpty(),
            prop = prop.toDomainModel()
        )
    } ?: JikanData.Aired()

    private fun JikanAnimeDetailsResponse.Prop?.toDomainModel() = this?.run {
        JikanData.Prop(
            from = from.toDomainModel(),
            to = to.toDomainModel()
        )
    } ?: JikanData.Prop()

    private fun JikanAnimeDetailsResponse.From?.toDomainModel() = this?.run {
        JikanData.From(
            day = day.orZero(),
            month = month.orZero(),
            year = year.orZero()
        )
    } ?: JikanData.From()

    private fun JikanAnimeDetailsResponse.To?.toDomainModel() = this?.run {
        JikanData.To(
            day = day.orZero(),
            month = month.orZero(),
            year = year.orZero()
        )
    } ?: JikanData.To()

    private fun JikanAnimeDetailsResponse.Broadcast?.toDomainModel() = this?.run {
        JikanData.Broadcast(
            day = day.orEmpty(),
            string = string.orEmpty(),
            time = time.orEmpty(),
            timezone = timezone.orEmpty()
        )
    } ?: JikanData.Broadcast()

    private fun List<JikanAnimeDetailsResponse.Demographic>?.toJikanDemographicDomainModel() =
        this.orEmpty().map {
            JikanData.Demographic(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.ExplicitGenres>?.toJikanExplicitGenresDomainModel() =
        this.orEmpty().map {
            JikanData.ExplicitGenres(
                malId = it.malId.orZero(),
                name = it.name.orEmpty(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Genre>?.toJikanGenreDomainModel() =
        this.orEmpty().map {
            JikanData.Genre(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun JikanAnimeDetailsResponse.Images?.toDomainModel() = this?.run {
        JikanData.Images(
            jpg = jpg.toDomainModel(),
            webp = webp.toDomainModel()
        )
    } ?: JikanData.Images()

    private fun JikanAnimeDetailsResponse.Jpg?.toDomainModel() = this?.run {
        JikanData.Jpg(
            imageUrl = imageUrl.orEmpty(),
            largeImageUrl = largeImageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanData.Jpg()

    private fun JikanAnimeDetailsResponse.Webp?.toDomainModel() = this?.run {
        JikanData.Webp(
            imageUrl = imageUrl.orEmpty(),
            largeImageUrl = largeImageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanData.Webp()

    private fun List<JikanAnimeDetailsResponse.Licensor>?.toJikanLicensorDomainModel() =
        this.orEmpty().map {
            JikanData.Licensor(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Producer>?.toJikanProducerDomainModel() =
        this.orEmpty().map {
            JikanData.Producer(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Studio>?.toJikanStudioDomainModel() =
        this.orEmpty().map {
            JikanData.Studio(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun JikanAnimeDetailsResponse.Theme?.mapToDomainModel() = this?.run {
        JikanData.Theme(
            openings = openings.orEmpty(),
            endings = endings.orEmpty()
        )
    } ?: JikanData.Theme()

    private fun List<JikanAnimeDetailsResponse.Themes>?.toJikanThemeDomainModel() =
        this.orEmpty().map {
            JikanData.Themes(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Title>?.toJikanTitleDomainModel() =
        this.orEmpty().map {
            JikanData.Title(
                title = it.title.orEmpty(),
                type = it.type.orEmpty()
            )
        }

    private fun JikanAnimeDetailsResponse.Trailer?.toDomainModel() = this?.run {
        JikanData.Trailer(
            embedUrl = embedUrl.orEmpty(),
            url = url.orEmpty(),
            youtubeId = youtubeId.orEmpty(),
            images = images.toDomainModel()
        )
    } ?: JikanData.Trailer()

    private fun JikanAnimeDetailsResponse.TrailerImages?.toDomainModel() = this?.run {
        JikanData.TrailerImages(
            imageUrl = imageUrl.orEmpty(),
            largeImageUrl = largeImageUrl.orEmpty(),
            maximumImageUrl = maximumImageUrl.orEmpty(),
            mediumImageUrl = mediumImageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanData.TrailerImages()

    private fun List<JikanVoiceActorsResponse.Data>?.toCastDomainModel() = this.orEmpty().map {
        JikanVAData(
            character = it.character.toDomainModel(),
            favorites = it.favorites.orZero(),
            role = it.role.orEmpty(),
            voiceActors = it.voiceActors.toVADomainModel().firstOrNull { va ->
                va.language.equals(Constant.JAPANESE, ignoreCase = true)
            } ?: JikanVAData.VoiceActor()
        )
    }

    private fun JikanVoiceActorsResponse.Character?.toDomainModel() = this?.run {
        JikanVAData.Character(
            images = images.toDomainModel(),
            malId = malId.orZero(),
            name = name.orEmpty(),
            url = url.orEmpty()
        )
    } ?: JikanVAData.Character()

    private fun JikanVoiceActorsResponse.Images?.toDomainModel() = this?.run {
        JikanVAData.Images(
            jpg = jpg.toDomainModel(),
            webp = webp.toDomainModel(),
        )
    } ?: JikanVAData.Images()

    private fun JikanVoiceActorsResponse.Jpg?.toDomainModel() = this?.run {
        JikanVAData.Jpg(
            imageUrl = imageUrl.orEmpty(),
        )
    } ?: JikanVAData.Jpg()

    private fun JikanVoiceActorsResponse.Webp?.toDomainModel() = this?.run {
        JikanVAData.Webp(
            imageUrl = imageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanVAData.Webp()

    private fun List<JikanVoiceActorsResponse.VoiceActor>?.toVADomainModel() = this.orEmpty().map {
        JikanVAData.VoiceActor(
            language = it.language.orEmpty(),
            person = it.person.toDomainModel()
        )
    }

    private fun JikanVoiceActorsResponse.Person?.toDomainModel() = this?.run {
        JikanVAData.Person(
            images = images.toDomainModel(),
            malId = malId.orZero(),
            name = name.orEmpty(),
            url = url.orEmpty()
        )
    } ?: JikanVAData.Person()

    private fun JikanVoiceActorsResponse.PersonImages?.toDomainModel() = this?.run {
        JikanVAData.PersonImages(
            jpg = jpg.toDomainModel()
        )
    } ?: JikanVAData.PersonImages()

    private fun JikanVoiceActorsResponse.PersonImagesJpg?.toDomainModel() = this?.run {
        JikanVAData.PersonImagesJpg(
            imageUrl = imageUrl.orEmpty()
        )
    } ?: JikanVAData.PersonImagesJpg()

    private fun List<JikanStaffResponse.Data>?.toStaffDomainModel() = this.orEmpty().map {
        JikanStaffData(
            person = it.person.toDomainModel(),
            positions = it.positions.orEmpty()
        )
    }

    private fun JikanStaffResponse.Person?.toDomainModel() = this?.run {
        JikanStaffData.Person(
            images = images.toDomainModel(),
            malId = malId.orZero(),
            name = name.orEmpty(),
            url = url.orEmpty()
        )
    } ?: JikanStaffData.Person()

    private fun JikanStaffResponse.Images?.toDomainModel() = this?.run {
        JikanStaffData.Images(
            jpg = jpg.toDomainModel()
        )
    } ?: JikanStaffData.Images()

    private fun JikanStaffResponse.Jpg?.toDomainModel() = this?.run {
        JikanStaffData.Jpg(
            imageUrl = imageUrl.orEmpty()
        )
    } ?: JikanStaffData.Jpg()

}
