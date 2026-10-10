package dev.fizcode.mediadetails.domain.model

internal data class JikanCastDomainModel(
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
