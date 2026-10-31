package dev.fizcode.mediadetails.presentation.model

import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface MediaDetailsIntent : UiIntent {
    data object RetryDetails : MediaDetailsIntent
    data object RetryCastAndStaff : MediaDetailsIntent
    data object RetryMangaDetails : MediaDetailsIntent
    data object RetryMangaCharacters : MediaDetailsIntent
    data object ToggleBookmark : MediaDetailsIntent
}
