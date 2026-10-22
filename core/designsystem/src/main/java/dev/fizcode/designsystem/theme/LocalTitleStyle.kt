package dev.fizcode.designsystem.theme

import androidx.compose.runtime.compositionLocalOf
import dev.fizcode.common.settings.TitleStyle

/** The title style the user picked in the settings, provided once at the root of the app. */
val LocalTitleStyle = compositionLocalOf { TitleStyle.DEFAULT }
