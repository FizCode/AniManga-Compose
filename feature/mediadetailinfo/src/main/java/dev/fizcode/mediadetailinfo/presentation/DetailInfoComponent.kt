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
import dev.fizcode.mediadetailinfo.model.AnimeCast
import dev.fizcode.mediadetailinfo.model.AnimeDetails
import dev.fizcode.mediadetailinfo.model.AnimeDetailsInfoUiModel
import dev.fizcode.mediadetailinfo.model.AnimeInfo
import dev.fizcode.mediadetailinfo.model.AnimeThemes
import dev.fizcode.mediadetailinfo.model.TabContents
import dev.fizcode.mediadetailinfo.presentation.component.CastTabComponent
import dev.fizcode.mediadetailinfo.presentation.component.DetailsTabComponent
import dev.fizcode.mediadetailinfo.presentation.component.InformationTabComponent
import dev.fizcode.mediadetailinfo.presentation.component.SongsTabComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInfoComponent(
    info: AnimeDetailsInfoUiModel,
    onClickShare: () -> Unit
) {
    val tabItems = TabContents.entries
    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            // Remove the divider line
            divider = {}
        ) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title.tabs,
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
                0 -> DetailsTabComponent(
                    animeDetails = info.animeDetails,
                    onClickShare = onClickShare
                )

                1 -> InformationTabComponent(animeInfo = info.animeInfo)
                2 -> CastTabComponent(cast = info.animeCast)
                3 -> SongsTabComponent(songThemes = info.animeThemes)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailInfoComponentPreview() {
    DetailInfoComponent(
        info = AnimeDetailsInfoUiModel(
            AnimeDetails(),
            AnimeInfo(),
            AnimeCast(),
            AnimeThemes()
        ),
        onClickShare = {}
    )
}
