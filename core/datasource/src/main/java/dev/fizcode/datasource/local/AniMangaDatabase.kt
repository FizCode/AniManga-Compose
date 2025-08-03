package dev.fizcode.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.fizcode.datasource.local.dao.BookmarkDAO
import dev.fizcode.datasource.local.model.BookmarkEntity
import dev.fizcode.datasource.utils.Constant

@Database(entities = [BookmarkEntity::class], version = 2, exportSchema = false)
abstract class AniMangaDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDAO

    companion object {
        @Volatile
        private var INSTANCE: AniMangaDatabase? = null

        fun getInstance(context: Context): AniMangaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AniMangaDatabase = Room.databaseBuilder(
            context = context.applicationContext,
            klass = AniMangaDatabase::class.java,
            name = Constant.ANIMANGA_DB_NAME
        )
            // TODO: Remove fallbackToDestructiveMigration when this goes to production
            .fallbackToDestructiveMigration(false)
            .build()
    }
}
