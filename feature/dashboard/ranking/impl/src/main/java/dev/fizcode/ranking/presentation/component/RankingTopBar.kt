package dev.fizcode.ranking.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.other.SearchTextFieldComponent
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.ranking.util.Constant

/**
 * Back button with the [title] and a search button. While [isSearching] the search field takes
 * the place of the title (and the search button goes away), the back button always stays.
 * It hides while the list scrolls down and shows again on any scroll up, driven by [scrollBehavior].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RankingTopBar(
    title: String,
    isSearching: Boolean,
    queryState: TextFieldState,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) = TopAppBar(
    scrollBehavior = scrollBehavior,
    title = {
        if (isSearching) {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
            SearchTextFieldComponent(
                modifier = Modifier.focusRequester(focusRequester),
                state = queryState,
                placeholder = Constant.PLACEHOLDER
            )
        } else {
            Text(text = title)
        }
    },
    navigationIcon = {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = CustomIcon.FILL_ARROW_BACK,
                contentDescription = Constant.BACK_ICON
            )
        }
    },
    actions = {
        if (!isSearching) {
            FilledTonalIconButton(
                modifier = Modifier.padding(end = 4.dp),
                onClick = onSearchClick
            ) {
                Icon(
                    imageVector = CustomIcon.FILL_SEARCH,
                    contentDescription = Constant.SEARCH_ICON
                )
            }
        } else {
            // Keeps the field clear of the screen edge, like the button does.
            Spacer(Modifier.padding(end = 12.dp))
        }
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RankingTopBarPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        RankingTopBar(
            title = "Top Airing ✨",
            isSearching = false,
            queryState = rememberTextFieldState(),
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            onBackClick = {},
            onSearchClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RankingTopBarSearchingPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        RankingTopBar(
            title = "Top Airing ✨",
            isSearching = true,
            queryState = rememberTextFieldState(initialText = "BLE"),
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            onBackClick = {},
            onSearchClick = {}
        )
    }
}
