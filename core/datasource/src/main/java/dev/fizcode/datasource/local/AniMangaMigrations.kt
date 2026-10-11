package dev.fizcode.datasource.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.fizcode.datasource.utils.Constant

/** Adds the other titles of a bookmark, existing bookmarks keep the default title only. */
internal val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        listOf("title_synonyms", "title_japanese", "title_english").forEach { column ->
            db.execSQL(
                "ALTER TABLE ${Constant.BOOKMARK_TABLE} ADD COLUMN $column TEXT NOT NULL DEFAULT ''"
            )
        }
    }
}
