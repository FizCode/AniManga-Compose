package dev.fizcode.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the [NavigationState].
 */
class Navigator(val state: NavigationState) {

    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            // Top-level route: switch to its stack.
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    /**
     * Goes back to [route] if it's already in the current stack, closing the screens above it,
     * otherwise opens it. Stops loops (A → B → A → B …) from piling up screens to back out of.
     */
    fun navigateOrPopTo(route: NavKey) {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val index = currentStack.lastIndexOf(route)
        if (index == -1) {
            currentStack.add(route)
        } else {
            repeat(currentStack.lastIndex - index) { currentStack.removeLastOrNull() }
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        // At the base of a stack, go back to the start route's stack.
        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
