package dev.fizcode.mediadetails.presentation.people.model

import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface PagedListIntent : UiIntent {
    data object LoadMore : PagedListIntent
    data object Retry : PagedListIntent
}
