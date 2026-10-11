package dev.fizcode.onboarding.data

import dev.fizcode.datastore.AniMangaDatastore
import dev.fizcode.onboarding.api.OnBoardingStatus
import kotlinx.coroutines.flow.first

internal class OnBoardingStatusImpl(
    private val datastore: AniMangaDatastore
) : OnBoardingStatus {

    override suspend fun isCompleted(): Boolean = datastore.getOnboarding().first()

    override suspend fun markCompleted() {
        datastore.setOnboarding(true)
    }
}
