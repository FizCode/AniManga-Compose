package dev.fizcode.mediadetails.domain.model

internal data class JikanStaffDomainModel(
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
