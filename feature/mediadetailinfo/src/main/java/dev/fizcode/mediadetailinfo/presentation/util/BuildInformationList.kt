package dev.fizcode.mediadetailinfo.presentation.util

import dev.fizcode.mediadetailinfo.model.AnimeInfo
import dev.fizcode.mediadetailinfo.model.TextTableData
import dev.fizcode.mediadetailinfo.util.Constant
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

internal fun buildInformationList(info: AnimeInfo): PersistentList<TextTableData> =
    persistentListOf(
        TextTableData(title = Constant.TYPE, desc = info.type),
        TextTableData(title = Constant.EPISODES, desc = info.episodes),
        TextTableData(title = Constant.STATUS, desc = info.status),
        TextTableData(title = Constant.AIRED, desc = info.aired),
        TextTableData(
            title = Constant.PREMIERED,
            desc = info.premiered,
            link = listOf(info.premieredUrl)
        ),
        TextTableData(title = Constant.BROADCAST, desc = info.aired),
        TextTableData(
            title = Constant.PRODUCERS,
            listDesc = info.producers.map { it.name },
            link = info.producers.map { it.link }),
        TextTableData(
            title = Constant.LICENSORS,
            listDesc = info.licensors.map { it.name },
            link = info.licensors.map { it.link }),
        TextTableData(
            title = Constant.STUDIOS,
            listDesc = info.studios.map { it.name },
            link = info.studios.map { it.link }),
        TextTableData(
            title = Constant.SOURCE,
            desc = info.source.name,
            link = listOf(info.source.link)
        ),
        TextTableData(
            title = Constant.GENRES,
            listDesc = info.genre.map { it.name },
            link = info.genre.map { it.link }),
        TextTableData(
            title = Constant.THEMES,
            listDesc = info.themes.map { it.name },
            link = info.themes.map { it.link }),
        TextTableData(title = Constant.DURATION, desc = info.duration),
        TextTableData(title = Constant.RATING, desc = info.rating)
    )
