package dev.fizcode.manga.presentation.mapper

import dev.fizcode.manga.domain.model.MangaDomainModel
import dev.fizcode.manga.presentation.model.MangaUiModel
import dev.fizcode.manga.util.Constant
import kotlinx.collections.immutable.toImmutableList
import java.util.Locale

internal class MangaUiMapper {

    fun mapToMangaUiModel(domainModel: MangaDomainModel): List<MangaUiModel> =
        domainModel.items.map { it.toUiModel() }

    private fun MangaDomainModel.Item.toUiModel() = MangaUiModel(
        id = id,
        mediaType = mediaType,
        mediaTypeLabel = mediaTypeLabel(mediaType),
        title = title,
        posterPath = posterPath,
        rating = String.format(Locale.US, "%.2f", mean),
        subTitle = "${Constant.VOLUME} ${count(numVolumes)} ${Constant.CHAPTER} ${count(numChapters)}" +
                " | ${statusLabel(status)}",
        synopsis = synopsis,
        genre = genres.toImmutableList()
    )

    /** MAL reports 0 while the total is not known yet. */
    private fun count(value: Int) = if (value == 0) Constant.UNKNOWN_COUNT else value.toString()

    private fun mediaTypeLabel(mediaType: String) = when (mediaType) {
        "manga" -> "Manga"
        "novel" -> "Novel"
        "light_novel" -> "Light Novel"
        "one_shot" -> "One-Shot"
        "doujinshi" -> "Doujinshi"
        "manhwa" -> "Manhwa"
        "manhua" -> "Manhua"
        "oel" -> "OEL"
        else -> Constant.UNKNOWN_MEDIA_TYPE
    }

    private fun statusLabel(status: String) = when (status) {
        "finished" -> "Finished"
        "currently_publishing" -> "Publishing"
        "not_yet_published" -> "Not Yet Published"
        "on_hiatus" -> "On Hiatus"
        "discontinued" -> "Discontinued"
        else -> Constant.UNKNOWN_STATUS
    }
}
