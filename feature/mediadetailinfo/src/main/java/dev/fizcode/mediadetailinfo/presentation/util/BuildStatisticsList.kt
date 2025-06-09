package dev.fizcode.mediadetailinfo.presentation.util

import dev.fizcode.mediadetailinfo.model.AnimeInfo
import dev.fizcode.mediadetailinfo.model.TextTableData
import dev.fizcode.mediadetailinfo.util.Constant
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

internal fun buildStatisticsList(info: AnimeInfo): PersistentList<TextTableData> = persistentListOf(
    TextTableData(title = Constant.SCORE, desc = info.score),
    TextTableData(title = Constant.RANKED, desc = info.ranked),
    TextTableData(title = Constant.POPULARITY, desc = info.popularity),
    TextTableData(title = Constant.MEMBERS, desc = info.members),
    TextTableData(title = Constant.FAVORITES, desc = info.favorites)
)
