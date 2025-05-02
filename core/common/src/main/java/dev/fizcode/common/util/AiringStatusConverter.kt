package dev.fizcode.common.util

/**
 * Convert snake case [status] to be status "Finished, Airing, Not Yet Aired"
 *
 * @param status String, should from [status] response
 */
fun airingStatus(status: String): String = when (status) {
    Constant.SNAKE_FINISHED_AIRING -> Constant.FINISHED_AIRING
    Constant.SNAKE_CURRENTLY_AIRING -> Constant.CURRENTLY_AIRING
    Constant.SNAKE_NOT_YET_AIRED -> Constant.NOT_YET_AIRED
    else -> Constant.UNKNOWN_AIRING_STATUS
}
