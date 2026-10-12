package dev.fizcode.bookmark.data.mapper

import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.datasource.local.model.BookmarkEntity
import dev.fizcode.common.settings.AlternativeTitles
import dev.fizcode.datasource.local.model.decodeSynonyms

internal class BookmarkDomainMapper {

    internal fun mapToBookmarkDomainModel(entity: List<BookmarkEntity>?) = entity.orEmpty().map {
        BookmarkDomainModel(
            mediaId = it.mediaId,
            mediaType = it.mediaType,
            posterPath = it.posterPath,
            rating = it.rating,
            title = it.title,
            episodes = it.episodes,
            status = it.status,
            studio = it.studio,
            genres = it.genres,
            alternativeTitles = AlternativeTitles(
                synonyms = decodeSynonyms(it.titleSynonyms),
                japanese = it.titleJapanese,
                english = it.titleEnglish
            )
        )
    }

}
