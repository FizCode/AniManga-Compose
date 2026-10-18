package dev.fizcode.mal.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A list MyAnimeList answers with: the season, ranking and search endpoints of anime and manga.
 * [T] is the media of each entry, which of its fields are filled depends on the `fields` asked for.
 */
@Serializable
data class MalListResponse<T>(
    @SerialName("data")
    val data: List<MalListItem<T>>? = null,
    @SerialName("paging")
    val paging: MalPaging? = null,
    /** Only the season endpoint sends it. */
    @SerialName("season")
    val season: MalSeason? = null
)

@Serializable
data class MalListItem<T>(
    @SerialName("node")
    val node: T? = null,
    /** Only the ranking endpoint sends it. */
    @SerialName("ranking")
    val ranking: MalRanking? = null
)

@Serializable
data class MalRanking(
    @SerialName("rank")
    val rank: Int? = null
)

@Serializable
data class MalPaging(
    @SerialName("next")
    val next: String? = null
)

@Serializable
data class MalSeason(
    @SerialName("year")
    val year: Int? = null,
    @SerialName("season")
    val season: String? = null
)
