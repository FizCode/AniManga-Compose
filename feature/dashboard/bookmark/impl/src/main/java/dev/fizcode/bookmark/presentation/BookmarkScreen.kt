package dev.fizcode.bookmark.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.bookmark.presentation.component.BookmarkHeaderComponent
import dev.fizcode.bookmark.presentation.component.BookmarkItemComponent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookmarkScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    bookmarkViewModel: BookmarkViewModel = koinViewModel()
) {

    val bookmarks by bookmarkViewModel.bookmarks.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    BookmarkHeaderComponent(
                        searchValue = "Search",
                        dropdownLabel = "All Content",
                        onClickSearch = {},
                        onClickDropdown = {}
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            BookmarkItemComponent(
                cardItem = bookmarks,
                onCardClick = onCardClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkScreenPreview() = BookmarkScreen({ _, _ -> })
