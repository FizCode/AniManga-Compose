package dev.fizcode.datastore.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.fizcode.datastore.AniMangaDatastore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

    @Singleton
    @Provides
    fun provideAniMangaDatastore(@ApplicationContext context: Context): AniMangaDatastore =
        AniMangaDatastore(context = context)
}
