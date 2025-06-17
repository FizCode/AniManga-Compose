package dev.fizcode.animanga

import android.app.Application
import dev.fizcode.animanga.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class AniMangaApp : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AniMangaApp)
            modules(appModule())
        }
    }
}
