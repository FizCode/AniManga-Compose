package dev.fizcode.anime.presentation.model

import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface AnimeIntent : UiIntent {
    data object Refresh : AnimeIntent
}
