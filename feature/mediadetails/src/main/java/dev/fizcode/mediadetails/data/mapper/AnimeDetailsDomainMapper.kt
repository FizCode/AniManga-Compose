package dev.fizcode.mediadetails.data.mapper

import dev.fizcode.common.util.extensions.orDash
import dev.fizcode.common.util.extensions.orDashList
import dev.fizcode.common.util.extensions.orZero
import dev.fizcode.mediadetails.data.response.JikanAnimeDetailsResponse
import dev.fizcode.mediadetails.data.response.JikanStaffResponse
import dev.fizcode.mediadetails.data.response.JikanVoiceActorsResponse
import dev.fizcode.mediadetails.data.response.MalAnimeDetailsResponse
import dev.fizcode.mediadetails.domain.model.AlternativeTitles
import dev.fizcode.mediadetails.domain.model.Broadcast
import dev.fizcode.mediadetails.domain.model.Genre
import dev.fizcode.mediadetails.domain.model.JikanAnimeDetailsDomainModel
import dev.fizcode.mediadetails.domain.model.JikanCastDomainModel
import dev.fizcode.mediadetails.domain.model.JikanStaffDomainModel
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
        detailsResponse: JikanAnimeDetailsResponse
    ): JikanAnimeDetailsDomainModel = detailsResponse.data?.run {
        JikanAnimeDetailsDomainModel(
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
    } ?: JikanAnimeDetailsDomainModel()

    private fun JikanAnimeDetailsResponse.Aired?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Aired(
            from = from.orEmpty(),
            string = string.orEmpty(),
            to = to.orEmpty(),
            prop = prop.toDomainModel()
        )
    } ?: JikanAnimeDetailsDomainModel.Aired()

    private fun JikanAnimeDetailsResponse.Prop?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Prop(
            from = from.toDomainModel(),
            to = to.toDomainModel()
        )
    } ?: JikanAnimeDetailsDomainModel.Prop()

    private fun JikanAnimeDetailsResponse.From?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.From(
            day = day.orZero(),
            month = month.orZero(),
            year = year.orZero()
        )
    } ?: JikanAnimeDetailsDomainModel.From()

    private fun JikanAnimeDetailsResponse.To?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.To(
            day = day.orZero(),
            month = month.orZero(),
            year = year.orZero()
        )
    } ?: JikanAnimeDetailsDomainModel.To()

    private fun JikanAnimeDetailsResponse.Broadcast?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Broadcast(
            day = day.orEmpty(),
            string = string.orEmpty(),
            time = time.orEmpty(),
            timezone = timezone.orEmpty()
        )
    } ?: JikanAnimeDetailsDomainModel.Broadcast()

    private fun List<JikanAnimeDetailsResponse.Demographic>?.toJikanDemographicDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.Demographic(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.ExplicitGenres>?.toJikanExplicitGenresDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.ExplicitGenres(
                malId = it.malId.orZero(),
                name = it.name.orEmpty(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Genre>?.toJikanGenreDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.Genre(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun JikanAnimeDetailsResponse.Images?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Images(
            jpg = jpg.toDomainModel(),
            webp = webp.toDomainModel()
        )
    } ?: JikanAnimeDetailsDomainModel.Images()

    private fun JikanAnimeDetailsResponse.Jpg?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Jpg(
            imageUrl = imageUrl.orEmpty(),
            largeImageUrl = largeImageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanAnimeDetailsDomainModel.Jpg()

    private fun JikanAnimeDetailsResponse.Webp?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Webp(
            imageUrl = imageUrl.orEmpty(),
            largeImageUrl = largeImageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanAnimeDetailsDomainModel.Webp()

    private fun List<JikanAnimeDetailsResponse.Licensor>?.toJikanLicensorDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.Licensor(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Producer>?.toJikanProducerDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.Producer(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Studio>?.toJikanStudioDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.Studio(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun JikanAnimeDetailsResponse.Theme?.mapToDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Theme(
            openings = openings.orEmpty(),
            endings = endings.orEmpty()
        )
    } ?: JikanAnimeDetailsDomainModel.Theme()

    private fun List<JikanAnimeDetailsResponse.Themes>?.toJikanThemeDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.Themes(
                malId = it.malId.orZero(),
                name = it.name.orDash(),
                type = it.type.orEmpty(),
                url = it.url.orEmpty()
            )
        }

    private fun List<JikanAnimeDetailsResponse.Title>?.toJikanTitleDomainModel() =
        this.orEmpty().map {
            JikanAnimeDetailsDomainModel.Title(
                title = it.title.orEmpty(),
                type = it.type.orEmpty()
            )
        }

    private fun JikanAnimeDetailsResponse.Trailer?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.Trailer(
            embedUrl = embedUrl.orEmpty(),
            url = url.orEmpty(),
            youtubeId = youtubeId.orEmpty(),
            images = images.toDomainModel()
        )
    } ?: JikanAnimeDetailsDomainModel.Trailer()

    private fun JikanAnimeDetailsResponse.TrailerImages?.toDomainModel() = this?.run {
        JikanAnimeDetailsDomainModel.TrailerImages(
            imageUrl = imageUrl.orEmpty(),
            largeImageUrl = largeImageUrl.orEmpty(),
            maximumImageUrl = maximumImageUrl.orEmpty(),
            mediumImageUrl = mediumImageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanAnimeDetailsDomainModel.TrailerImages()

    internal fun mapToCastDomainModel(cast: JikanVoiceActorsResponse) = cast.data.orEmpty().map {
        JikanCastDomainModel(
            character = it.character.toDomainModel(),
            favorites = it.favorites.orZero(),
            role = it.role.orEmpty(),
            voiceActors = it.voiceActors.toVADomainModel().firstOrNull { va ->
                va.language.equals(Constant.JAPANESE, ignoreCase = true)
            } ?: JikanCastDomainModel.VoiceActor()
        )
    }

    private fun JikanVoiceActorsResponse.Character?.toDomainModel() = this?.run {
        JikanCastDomainModel.Character(
            images = images.toDomainModel(),
            malId = malId.orZero(),
            name = name.orEmpty(),
            url = url.orEmpty()
        )
    } ?: JikanCastDomainModel.Character()

    private fun JikanVoiceActorsResponse.Images?.toDomainModel() = this?.run {
        JikanCastDomainModel.Images(
            jpg = jpg.toDomainModel(),
            webp = webp.toDomainModel(),
        )
    } ?: JikanCastDomainModel.Images()

    private fun JikanVoiceActorsResponse.Jpg?.toDomainModel() = this?.run {
        JikanCastDomainModel.Jpg(
            imageUrl = imageUrl.orEmpty(),
        )
    } ?: JikanCastDomainModel.Jpg()

    private fun JikanVoiceActorsResponse.Webp?.toDomainModel() = this?.run {
        JikanCastDomainModel.Webp(
            imageUrl = imageUrl.orEmpty(),
            smallImageUrl = smallImageUrl.orEmpty()
        )
    } ?: JikanCastDomainModel.Webp()

    private fun List<JikanVoiceActorsResponse.VoiceActor>?.toVADomainModel() = this.orEmpty().map {
        JikanCastDomainModel.VoiceActor(
            language = it.language.orEmpty(),
            person = it.person.toDomainModel()
        )
    }

    private fun JikanVoiceActorsResponse.Person?.toDomainModel() = this?.run {
        JikanCastDomainModel.Person(
            images = images.toDomainModel(),
            malId = malId.orZero(),
            name = name.orEmpty(),
            url = url.orEmpty()
        )
    } ?: JikanCastDomainModel.Person()

    private fun JikanVoiceActorsResponse.PersonImages?.toDomainModel() = this?.run {
        JikanCastDomainModel.PersonImages(
            jpg = jpg.toDomainModel()
        )
    } ?: JikanCastDomainModel.PersonImages()

    private fun JikanVoiceActorsResponse.PersonImagesJpg?.toDomainModel() = this?.run {
        JikanCastDomainModel.PersonImagesJpg(
            imageUrl = imageUrl.orEmpty()
        )
    } ?: JikanCastDomainModel.PersonImagesJpg()

    internal fun mapToStaffDomainModel(staff: JikanStaffResponse) = staff.data.orEmpty().map {
        JikanStaffDomainModel(
            person = it.person.toDomainModel(),
            positions = it.positions.orEmpty()
        )
    }

    private fun JikanStaffResponse.Person?.toDomainModel() = this?.run {
        JikanStaffDomainModel.Person(
            images = images.toDomainModel(),
            malId = malId.orZero(),
            name = name.orEmpty(),
            url = url.orEmpty()
        )
    } ?: JikanStaffDomainModel.Person()

    private fun JikanStaffResponse.Images?.toDomainModel() = this?.run {
        JikanStaffDomainModel.Images(
            jpg = jpg.toDomainModel()
        )
    } ?: JikanStaffDomainModel.Images()

    private fun JikanStaffResponse.Jpg?.toDomainModel() = this?.run {
        JikanStaffDomainModel.Jpg(
            imageUrl = imageUrl.orEmpty()
        )
    } ?: JikanStaffDomainModel.Jpg()

}
