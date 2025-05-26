package dev.fizcode.mediadetails.domain.model

internal data class JikanAnimeDetailsDomainModel(
    val data: JikanData,
    val voiceActors: List<JikanVAData>,
    val staffs: List<JikanStaffData>
)

internal data class JikanData(
    val aired: Aired = Aired(),
    val airing: Boolean = false,
    val approved: Boolean = false,
    val background: String = "",
    val broadcast: Broadcast = Broadcast(),
    val demographics: List<Demographic> = emptyList(),
    val duration: String = "",
    val episodes: Int = 0,
    val explicitGenres: List<ExplicitGenres> = emptyList(),
    val external: List<External> = emptyList(),
    val favorites: Int = 0,
    val genres: List<Genre> = emptyList(),
    val images: Images = Images(),
    val licensors: List<Licensor> = emptyList(),
    val malId: Int = 0,
    val members: Int = 0,
    val popularity: Int = 0,
    val producers: List<Producer> = emptyList(),
    val rank: Int = 0,
    val rating: String = "",
    val relations: List<Relation> = emptyList(),
    val score: Double = 0.0,
    val scoredBy: Int = 0,
    val season: String = "",
    val source: String = "",
    val status: String = "",
    val streaming: List<Streaming> = emptyList(),
    val studios: List<Studio> = emptyList(),
    val synopsis: String = "",
    val theme: Theme = Theme(),
    val themes: List<Themes> = emptyList(),
    val title: String = "",
    val titleEnglish: String = "",
    val titleJapanese: String = "",
    val titleSynonyms: List<String> = emptyList(),
    val titles: List<Title> = emptyList(),
    val trailer: Trailer = Trailer(),
    val type: String = "",
    val url: String = "",
    val year: Int = 0
) {

    data class Aired(
        val from: String = "",
        val prop: Prop = Prop(),
        val string: String = "",
        val to: String = ""
    )

    data class Prop(
        val from: From = From(),
        val to: To = To()
    )

    data class From(
        val day: Int = 0,
        val month: Int = 0,
        val year: Int = 0
    )

    data class To(
        val day: Int = 0,
        val month: Int = 0,
        val year: Int = 0
    )

    data class Broadcast(
        val day: String = "",
        val string: String = "",
        val time: String = "",
        val timezone: String = ""
    )

    data class Demographic(
        val malId: Int = 0,
        val name: String = "",
        val type: String = "",
        val url: String = ""
    )

    data class ExplicitGenres(
        val malId: Int = 0,
        val name: String = "",
        val type: String = "",
        val url: String = ""
    )

    data class External(
        val name: String = "",
        val url: String = ""
    )

    data class Genre(
        val malId: Int = 0,
        val name: String = "",
        val type: String = "",
        val url: String = ""
    )

    data class Images(
        val jpg: Jpg = Jpg(),
        val webp: Webp = Webp()
    )

    data class Jpg(
        val imageUrl: String = "",
        val largeImageUrl: String = "",
        val smallImageUrl: String = ""
    )

    data class Webp(
        val imageUrl: String = "",
        val largeImageUrl: String = "",
        val smallImageUrl: String = ""
    )

    data class Licensor(
        val malId: Int = 0,
        val name: String = "",
        val type: String = "",
        val url: String = ""
    )

    data class Producer(
        val malId: Int = 0,
        val name: String = "",
        val type: String = "",
        val url: String = ""
    )

    data class Relation(
        val entry: List<Entry> = emptyList(),
        val relation: String = ""
    ) {
        data class Entry(
            val malId: Int = 0,
            val name: String = "",
            val type: String = "",
            val url: String = ""
        )
    }

    data class Streaming(
        val name: String = "",
        val url: String = ""
    )

    data class Studio(
        val malId: Int = 0,
        val name: String = "",
        val type: String = "",
        val url: String = ""
    )

    data class Theme(
        val endings: List<String> = emptyList(),
        val openings: List<String> = emptyList()
    )

    data class Themes(
        val malId: Int = 0,
        val name: String = "",
        val type: String = "",
        val url: String = ""
    )

    data class Title(
        val title: String = "",
        val type: String = ""
    )

    data class Trailer(
        val embedUrl: String = "",
        val images: TrailerImages = TrailerImages(),
        val url: String = "",
        val youtubeId: String = ""
    )

    data class TrailerImages(
        val imageUrl: String = "",
        val largeImageUrl: String = "",
        val maximumImageUrl: String = "",
        val mediumImageUrl: String = "",
        val smallImageUrl: String = ""
    )
}

internal data class JikanVAData(
    val character: Character = Character(),
    val favorites: Int = 0,
    val role: String = "",
    val voiceActors: VoiceActor = VoiceActor()
) {
    data class Character(
        val images: Images = Images(),
        val malId: Int = 0,
        val name: String = "",
        val url: String = ""
    )

    data class Images(
        val jpg: Jpg = Jpg(),
        val webp: Webp = Webp()
    )

    data class Jpg(
        val imageUrl: String = ""
    )

    data class Webp(
        val imageUrl: String = "",
        val smallImageUrl: String = ""
    )

    data class VoiceActor(
        val language: String = "",
        val person: Person = Person()
    )

    data class Person(
        val images: PersonImages = PersonImages(),
        val malId: Int = 0,
        val name: String = "",
        val url: String = ""
    )

    data class PersonImages(
        val jpg: PersonImagesJpg = PersonImagesJpg()
    )

    data class PersonImagesJpg(
        val imageUrl: String = ""
    )
}

internal data class JikanStaffData(
    val person: Person = Person(),
    val positions: List<String> = emptyList()
) {
    data class Person(
        val images: Images = Images(),
        val malId: Int = 0,
        val name: String = "",
        val url: String = ""
    )

    data class Images(
        val jpg: Jpg = Jpg()
    )

    data class Jpg(
        val imageUrl: String = ""
    )
}
