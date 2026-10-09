package dev.fizcode.mediadetails.presentation.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetails
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetailsInfoUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeInfo
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeThemes
import dev.fizcode.mediadetails.presentation.info.model.TabContents
import dev.fizcode.mediadetails.presentation.info.component.CastTabComponent
import dev.fizcode.mediadetails.presentation.info.component.DetailsTabComponent
import dev.fizcode.mediadetails.presentation.info.component.InformationTabComponent
import dev.fizcode.mediadetails.presentation.info.component.SongsTabComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInfoComponent(
    infoUiModel: AnimeDetailsInfoUiModel,
    cast: UiState<ImmutableList<AnimeCastUiModel>>,
    staff: UiState<ImmutableList<AnimeStaffUiModel>>,
    onClickShare: (String) -> Unit
) {
    val tabItems = TabContents.entries
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabItems.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            // Let divider empty set! It will remove the divider line
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
            key(position) {
                when (position) {
                    0 -> DetailsTabComponent(
                        animeDetails = infoUiModel.animeDetails,
                        onClickShare = onClickShare
                    )

                    1 -> InformationTabComponent(animeInfo = infoUiModel.animeInfo)
                    2 -> CastTabComponent(
                        castUiModel = cast,
                        staffUiModel = staff,
                        onMoreClick = {},
                        onCastImageClick = {}
                    )

                    3 -> SongsTabComponent(songThemes = infoUiModel.animeThemes)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailInfoComponentPreview() {
    DetailInfoComponent(
        infoUiModel = AnimeDetailsInfoUiModel(
            AnimeDetails(),
            AnimeInfo(),
            AnimeThemes()
        ),
        cast = UiState.Loading,
        staff = UiState.Loading,
        onClickShare = {}
    )
}
