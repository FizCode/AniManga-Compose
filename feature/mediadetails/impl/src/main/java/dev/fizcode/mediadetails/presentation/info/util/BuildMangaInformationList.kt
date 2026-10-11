package dev.fizcode.mediadetails.presentation.info.util

import dev.fizcode.mediadetails.presentation.info.model.MangaInfo
import dev.fizcode.mediadetails.presentation.info.model.TextTableData
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

internal fun buildMangaInformationList(info: MangaInfo): PersistentList<TextTableData> =
    persistentListOf(
        TextTableData(title = Constant.TYPE, desc = info.type),
        TextTableData(title = Constant.VOLUMES, desc = info.volumes),
        TextTableData(title = Constant.CHAPTERS, desc = info.chapters),
        TextTableData(title = Constant.STATUS, desc = info.status),
        TextTableData(title = Constant.PUBLISHED, desc = info.published),
        TextTableData(
            title = Constant.AUTHORS,
            listDesc = info.authors.map { it.name },
            link = info.authors.map { it.link }),
        TextTableData(
            title = Constant.SERIALIZATION,
            listDesc = info.serialization.map { it.name },
            link = info.serialization.map { it.link }),
        TextTableData(
            title = Constant.GENRES,
            listDesc = info.genre.map { it.name },
            link = info.genre.map { it.link }),
        TextTableData(
            title = Constant.THEMES,
            listDesc = info.themes.map { it.name },
            link = info.themes.map { it.link })
    )

internal fun buildMangaStatisticsList(info: MangaInfo): PersistentList<TextTableData> =
    persistentListOf(
        TextTableData(title = Constant.SCORE, desc = info.score),
        TextTableData(title = Constant.RANKED, desc = info.ranked),
        TextTableData(title = Constant.POPULARITY, desc = info.popularity),
        TextTableData(title = Constant.MEMBERS, desc = info.members),
        TextTableData(title = Constant.FAVORITES, desc = info.favorites)
    )
