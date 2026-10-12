package dev.fizcode.bookmark.data.repository

import dev.fizcode.bookmark.data.mapper.BookmarkDomainMapper
import dev.fizcode.bookmark.data.model.BookmarkDomainModel
import dev.fizcode.bookmark.domain.repository.BookmarkRepository
import dev.fizcode.common.base.callhandler.DomainLocalState
import dev.fizcode.common.base.callhandler.processDatabaseCall
import dev.fizcode.database.bookmark.BookmarkLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BookmarkRepositoryImpl(
    private val bookmarkLocalDataSource: BookmarkLocalDataSource,
    private val bookmarkDomainMapper: BookmarkDomainMapper
) : BookmarkRepository {

    override fun getAllBookmarks(): Flow<DomainLocalState<List<BookmarkDomainModel>>> =
        processDatabaseCall {
            bookmarkLocalDataSource.getAllBookmarks().map { records ->
                bookmarkDomainMapper.mapToBookmarkDomainModel(records = records)
            }
        }

}