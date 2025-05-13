package dev.fizcode.mediadetailinfo.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.fizcode.mediadetailinfo.presentation.component.DetailsTabComponent
import dev.fizcode.mediadetailinfo.presentation.component.InformationTabComponent
import dev.fizcode.mediadetailinfo.presentation.component.SongsTabComponent
import dev.fizcode.mediadetailinfo.presentation.component.StaffTabComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInfoComponent() {
    val titles = listOf("Details", "Info", "VA & Staff", "Songs")
    val pagerState = rememberPagerState(pageCount = {titles.size})
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            // Remove the divider line
            divider = {}
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        // Scroll to selected page
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        HorizontalPager(
            verticalAlignment = Alignment.Top,
            state = pagerState
        ) { position ->
            when (position) {
                0 -> DetailsTabComponent()
                1 -> InformationTabComponent()
                2 -> StaffTabComponent()
                3 -> SongsTabComponent()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailInfoComponentPreview() {
    DetailInfoComponent()
}
