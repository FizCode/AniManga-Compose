package dev.fizcode.settings.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.fizcode.common.settings.NsfwFilter
import dev.fizcode.common.settings.ThemeMode
import dev.fizcode.common.settings.TitleStyle
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.settings.util.Constant

/** Name of a theme option in the chooser. */
internal val ThemeMode.label: String
    get() = when (this) {
        ThemeMode.DEFAULT -> "Default"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }

/** What the theme row shows as its current value. */
internal val ThemeMode.summary: String
    get() = when (this) {
        ThemeMode.DEFAULT -> "Follow the System"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }

internal val ThemeMode.icon: ImageVector
    get() = when (this) {
        ThemeMode.DEFAULT -> CustomIcon.FILL_BRIGHTNESS_AUTO
        ThemeMode.LIGHT -> CustomIcon.FILL_LIGHT_MODE
        ThemeMode.DARK -> CustomIcon.FILL_DARK_MODE
    }

internal val TitleStyle.label: String
    get() = when (this) {
        TitleStyle.DEFAULT -> "Default"
        TitleStyle.SYNONYM -> "Synonym"
        TitleStyle.JAPANESE -> "Japanese"
        TitleStyle.ENGLISH -> "English"
    }

internal val TitleStyle.description: String
    get() = when (this) {
        TitleStyle.DEFAULT -> "Media titles should be a default title style."
        TitleStyle.SYNONYM ->
            "Media titles use their synonym. Media without a synonym keep the default title."
        TitleStyle.JAPANESE -> "Media titles are shown in Japanese."
        TitleStyle.ENGLISH -> "Media titles are shown in English."
    }

internal val TitleStyle.icon: ImageVector
    get() = when (this) {
        TitleStyle.DEFAULT -> CustomIcon.FILL_STARS
        TitleStyle.SYNONYM -> CustomIcon.FILL_EDIT_NOTE
        TitleStyle.JAPANESE -> CustomIcon.FILL_TRANSLATE
        TitleStyle.ENGLISH -> CustomIcon.FILL_ABC
    }

internal val NsfwFilter.label: String
    get() = when (this) {
        NsfwFilter.SAFE -> "Safe"
        NsfwFilter.GREY -> "Grey"
        NsfwFilter.NOT_SAFE -> "Not Safe"
    }

internal val NsfwFilter.description: String
    get() = when (this) {
        NsfwFilter.SAFE -> "The contents is safe for work."
        NsfwFilter.GREY -> "The contents may include suggestive media. For ages 17+."
        NsfwFilter.NOT_SAFE -> "The contents may include explicit media. For ages 21+."
    }

/** A reminder of the minimum age, shown when the user is about to pick the level. */
internal val NsfwFilter.ageWarning: String?
    get() = when (this) {
        NsfwFilter.SAFE -> null
        NsfwFilter.GREY -> "Warning: only continue if you are 17 years old or older."
        NsfwFilter.NOT_SAFE -> "Warning: only continue if you are 21 years old or older."
    }

/** A filter change the user still has to confirm. */
internal sealed interface PendingFilterChange {
    val dialogTitle: String
    val label: String
    val description: String

    /** An extra warning to show in the dialog, if the change needs one. */
    val warning: String?

    data class Titling(val style: TitleStyle) : PendingFilterChange {
        override val dialogTitle = Constant.TITLING_DIALOG_TITLE
        override val label = style.label
        override val description = style.description
        override val warning: String? = null
    }

    data class Nsfw(val filter: NsfwFilter) : PendingFilterChange {
        override val dialogTitle = Constant.NSFW_DIALOG_TITLE
        override val label = filter.label
        override val description = filter.description
        override val warning = filter.ageWarning
    }
}
