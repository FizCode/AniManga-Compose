package dev.fizcode.ranking.presentation.model

import dev.fizcode.common.base.mvi.UiIntent

internal sealed interface RankingIntent : UiIntent {
    data class QueryChanged(val query: String) : RankingIntent
    data object Retry : RankingIntent
}
