package dev.fizcode.datastore

import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.NsfwFilterProvider
import kotlinx.coroutines.flow.Flow

internal class DatastoreNsfwFilterProvider(
    datastore: AniMangaDatastore
) : NsfwFilterProvider {

    override val nsfwFilter: Flow<NsfwFilter> = datastore.getNsfwFilter()
}
