package dev.fizcode.bookmark.domain.repository

import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.common.base.callhandler.DomainLocalState
import kotlinx.coroutines.flow.Flow

internal interface BookmarkRepository {

    fun getAllBookmarks(): Flow<DomainLocalState<List<BookmarkDomainModel>>>

}