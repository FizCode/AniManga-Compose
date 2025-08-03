package dev.fizcode.common.util.extensions

import dev.fizcode.common.util.InternalConstant

fun releaseInfo(
    broadcastDay: String,
    broadcastTime: String,
    status: String,
    numEpisodes: Int
): String {

    val formattedNumEps = when (numEpisodes) {
        0 -> InternalConstant.FINISHED_AIRING
        1 -> "$numEpisodes ${InternalConstant.EPISODE} | ${InternalConstant.FINISHED_AIRING}"
        else -> "$numEpisodes ${InternalConstant.EPISODES} | ${InternalConstant.FINISHED_AIRING}"
    }

    val formattedBroadcastDay = if (broadcastDay.isNotEmpty()) {
        "${broadcastDay.toCapitalFirstChar()}, $broadcastTime (${InternalConstant.JST})"
    } else {
        InternalConstant.UNKNOWN_AIRING_TIME
    }

    return when (status) {
        InternalConstant.SNAKE_FINISHED_AIRING -> formattedNumEps
        InternalConstant.SNAKE_CURRENTLY_AIRING -> formattedBroadcastDay
        InternalConstant.SNAKE_NOT_YET_AIRED -> InternalConstant.NOT_YET_AIRED
        else -> InternalConstant.UNKNOWN_AIRING_STATUS
    }
}
