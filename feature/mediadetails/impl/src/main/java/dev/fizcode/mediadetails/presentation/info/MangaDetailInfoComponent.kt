package dev.fizcode.mediadetails.presentation.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import dev.fizcode.mediadetails.presentation.info.component.CharactersTabComponent
import dev.fizcode.mediadetails.presentation.info.component.DetailsTabComponent
import dev.fizcode.mediadetails.presentation.info.component.MangaInformationTabComponent
import dev.fizcode.mediadetails.presentation.info.model.AnimeDetails
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaDetailsInfoUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaInfo
import dev.fizcode.mediadetails.presentation.info.model.MangaTabContents
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData

@Composable
fun MangaDetailInfoComponent(
    infoUiModel: MangaDetailsInfoUiModel,
    characters: UiState<ImmutableList<MangaCharacterUiModel>>,
    onRetryCharacters: () -> Unit,
    onMoreCharactersClick: () -> Unit,
    onClickShare: (String) -> Unit
) {
    val tabItems = MangaTabContents.entries
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
            tabItems.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            text = tab.tabs,
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
                when (tabItems[position]) {
                    MangaTabContents.DETAILS -> DetailsTabComponent(
                        animeDetails = infoUiModel.mangaDetails,
                        onClickShare = onClickShare
                    )

                    MangaTabContents.INFO -> MangaInformationTabComponent(
                        mangaInfo = infoUiModel.mangaInfo,
                        isExtendedInfoAvailable = infoUiModel.isExtendedInfoAvailable
                    )

                    MangaTabContents.CHARACTERS -> CharactersTabComponent(
                        characters = characters,
                        onRetry = onRetryCharacters,
                        onMoreClick = onMoreCharactersClick,
                        onCharacterClick = {}
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailInfoComponentPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaDetailInfoComponent(
            infoUiModel = MediaDetailsPreviewData.manga.mangaDetailsInfoUiModel,
            characters = UiState.Success(MediaDetailsPreviewData.mangaCharacters),
            onRetryCharacters = {},
            onMoreCharactersClick = {},
            onClickShare = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailInfoComponentLoadingPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaDetailInfoComponent(
            infoUiModel = MediaDetailsPreviewData.manga.mangaDetailsInfoUiModel,
            characters = UiState.Loading,
            onRetryCharacters = {},
            onMoreCharactersClick = {},
            onClickShare = {}
        )
    }
}
