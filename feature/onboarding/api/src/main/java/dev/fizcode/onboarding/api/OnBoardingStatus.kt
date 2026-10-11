package dev.fizcode.onboarding.api

/**
 * Whether the user has already been through the onboarding. It is stored on the device, so it
 * is only reset when the app data is cleared or the app is reinstalled.
 */
interface OnBoardingStatus {

    suspend fun isCompleted(): Boolean

    suspend fun markCompleted()
}
