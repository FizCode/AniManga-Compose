package dev.fizcode.common.util.extensions

import dev.fizcode.common.media.AiringStatus
import dev.fizcode.common.util.InternalConstant

fun releaseInfo(
    broadcastDay: String,
    broadcastTime: String,
    status: String,
    numEpisodes: Int
): String {

    val formattedNumEps = when (numEpisodes) {
        0 -> AiringStatus.FINISHED.label
        1 -> "$numEpisodes ${InternalConstant.EPISODE} | ${AiringStatus.FINISHED.label}"
        else -> "$numEpisodes ${InternalConstant.EPISODES} | ${AiringStatus.FINISHED.label}"
    }

    val formattedBroadcastDay = if (broadcastDay.isNotEmpty()) {
        "${broadcastDay.toCapitalFirstChar()}, $broadcastTime (${InternalConstant.JST})"
    } else {
        InternalConstant.UNKNOWN_AIRING_TIME
    }

    return when (val airingStatus = AiringStatus.fromApi(status)) {
        AiringStatus.FINISHED -> formattedNumEps
        AiringStatus.CURRENTLY_AIRING -> formattedBroadcastDay
        AiringStatus.NOT_YET_AIRED, AiringStatus.UNKNOWN -> airingStatus.label
    }
}
