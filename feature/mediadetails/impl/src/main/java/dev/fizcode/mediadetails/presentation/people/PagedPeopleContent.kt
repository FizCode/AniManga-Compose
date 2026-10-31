package dev.fizcode.mediadetails.presentation.people

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.card.EmptyCardComponent
import dev.fizcode.designsystem.component.other.ErrorWithRetry
import dev.fizcode.designsystem.component.other.UnavailableNotice
import dev.fizcode.designsystem.icon.CustomIcon
import dev.fizcode.mediadetails.presentation.people.model.PagedItems
import dev.fizcode.mediadetails.util.Constant

/**
 * A list of people loaded page by page, below a top bar with a back button and the [title].
 * The next page is asked for when the user comes close to the end of what is loaded.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <U> PagedPeopleContent(
    title: String,
    state: UiState<PagedItems<U>>,
    emptyMessage: String,
    onBackClick: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    shimmer: @Composable () -> Unit,
    itemContent: @Composable (U) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = CustomIcon.FILL_ARROW_BACK,
                            contentDescription = Constant.BACK_BUTTON
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (state) {
                // Empty is also the state before loading starts, so it shows the same placeholder.
                is UiState.Loading -> Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(LOADING_ROWS) { shimmer() }
                }

                is UiState.Success -> PeopleList(
                    paged = state.data,
                    onLoadMore = onLoadMore,
                    onRetry = onRetry,
                    itemContent = itemContent
                )

                is UiState.Empty -> EmptyCardComponent(
                    modifier = Modifier.fillMaxSize(),
                    desc = emptyMessage
                )

                else -> ErrorWithRetry(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = onRetry
                )
            }
        }
    }
}

@Composable
private fun <U> PeopleList(
    paged: PagedItems<U>,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    itemContent: @Composable (U) -> Unit
) {
    val listState = rememberLazyListState()
    val isNearEnd by remember(listState) {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= listState.layoutInfo.totalItemsCount - Constant.PAGE_LOAD_MORE_THRESHOLD
        }
    }

    // Also runs again after a page was added, in case the list is still not long enough to scroll.
    LaunchedEffect(isNearEnd, paged.items.size, paged.hasNextPage) {
        if (isNearEnd && paged.hasNextPage && !paged.loadMoreFailed) onLoadMore()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = paged.items) { item -> itemContent(item) }

        item {
            when {
                paged.isLoadingMore -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                paged.loadMoreFailed -> UnavailableNotice(
                    message = Constant.LOAD_MORE_FAILED,
                    onRetry = onRetry
                )
            }
        }
    }
}

private const val LOADING_ROWS = 8
