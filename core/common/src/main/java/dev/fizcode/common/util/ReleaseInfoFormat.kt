package dev.fizcode.common.util

fun releaseInfo(
    broadcastDay: String,
    broadcastTime: String,
    status: String,
    numEpisodes: Int
): String {

    val formattedNumEps = when (numEpisodes) {
        0 -> Constant.FINISHED_AIRING
        1 -> "$numEpisodes ${Constant.EPISODE} | ${Constant.FINISHED_AIRING}"
        else -> "$numEpisodes ${Constant.EPISODES} | ${Constant.FINISHED_AIRING}"
    }

    val formattedBroadcastDay = if (broadcastDay.isNotEmpty()) {
        "${broadcastDay.toCapitalFirstChar()}, $broadcastTime (${Constant.JST})"
    } else {
        Constant.UNKNOWN_AIRING_TIME
    }

    return when (status) {
        Constant.SNAKE_FINISHED_AIRING -> formattedNumEps
        Constant.SNAKE_CURRENTLY_AIRING -> formattedBroadcastDay
        Constant.SNAKE_NOT_YET_AIRED -> Constant.NOT_YET_AIRED
        else -> Constant.UNKNOWN_AIRING_STATUS
    }
}
