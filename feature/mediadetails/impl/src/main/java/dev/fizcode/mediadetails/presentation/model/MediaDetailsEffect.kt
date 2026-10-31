package dev.fizcode.mediadetails.presentation.model

import dev.fizcode.common.base.mvi.UiEffect

internal sealed interface MediaDetailsEffect : UiEffect {
    data object Bookmarked : MediaDetailsEffect
    data object Unbookmarked : MediaDetailsEffect
}
