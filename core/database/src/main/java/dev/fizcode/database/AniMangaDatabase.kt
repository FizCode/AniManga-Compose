package dev.fizcode.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.fizcode.database.bookmark.BookmarkDAO
import dev.fizcode.database.bookmark.BookmarkEntity

/** Only this module sees the database, other modules use the local data sources it provides. */
@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = true)
internal abstract class AniMangaDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDAO
}
