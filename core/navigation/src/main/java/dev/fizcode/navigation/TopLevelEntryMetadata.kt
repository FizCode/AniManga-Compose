package dev.fizcode.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay

/**
 * Entry metadata for top-level (bottom bar) destinations: switching between them cross-fades
 * instead of using the horizontal slide used for pushing screens.
 */
fun topLevelEntryMetadata(): Map<String, Any> = metadata {
    put(NavDisplay.TransitionKey) { fadeIn() togetherWith fadeOut() }
    put(NavDisplay.PopTransitionKey) { fadeIn() togetherWith fadeOut() }
    put(NavDisplay.PredictivePopTransitionKey) { fadeIn() togetherWith fadeOut() }
}
