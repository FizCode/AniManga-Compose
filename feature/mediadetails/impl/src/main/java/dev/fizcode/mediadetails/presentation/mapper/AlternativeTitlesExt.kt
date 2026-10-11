package dev.fizcode.mediadetails.presentation.mapper

import dev.fizcode.mediadetails.domain.model.AlternativeTitles

internal fun AlternativeTitles.toCommon() = dev.fizcode.common.settings.AlternativeTitles(
    synonyms = synonyms,
    japanese = ja,
    english = en
)
