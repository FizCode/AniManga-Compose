package dev.fizcode.mediadetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.other.ErrorWithRetry
import dev.fizcode.mediadetails.presentation.header.MangaDetailHeaderComponent
import dev.fizcode.mediadetails.presentation.info.MangaDetailInfoComponent
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.presentation.model.MangaDetailsUiModel
import kotlinx.collections.immutable.ImmutableList
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData
import androidx.compose.ui.tooling.preview.Preview
import dev.fizcode.common.settings.MediaTitles

@Composable
internal fun MangaDetailsComponent(
    modifier: Modifier = Modifier,
    isBookmarked: Boolean,
    mangaDetails: UiState<MangaDetailsUiModel>,
    characters: UiState<ImmutableList<MangaCharacterUiModel>>,
    headerTitle: (MediaTitles) -> Unit,
    selectedImage: (String) -> Unit,
    onRetryDetails: () -> Unit,
    onRetryCharacters: () -> Unit,
    onMoreCharactersClick: () -> Unit,
    onClickBookmark: (MangaDetailsUiModel) -> Unit,
    onClickShare: (String) -> Unit
) = LazyColumn(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    when (mangaDetails) {
        is UiState.Success -> {
            headerTitle(mangaDetails.data.mangaDetailsHeaderUiModel.titles)
            item {
                MangaDetailHeaderComponent(
                    modifier = modifier,
                    isBookmarked = isBookmarked,
                    header = mangaDetails.data.mangaDetailsHeaderUiModel,
                    onPictureClick = selectedImage,
                    onClickBookmark = { onClickBookmark(mangaDetails.data) }
                )
            }
            item {
                MangaDetailInfoComponent(
                    infoUiModel = mangaDetails.data.mangaDetailsInfoUiModel,
                    characters = characters,
                    onRetryCharacters = onRetryCharacters,
                    onMoreCharactersClick = onMoreCharactersClick,
                    onClickShare = onClickShare
                )
            }
        }

        // Empty is also the state before loading starts, so it shows the same placeholder.
        is UiState.Loading, is UiState.Empty -> item { AnimeDetailsShimmer() }

        else -> item {
            ErrorWithRetry(
                modifier = Modifier.fillParentMaxSize(),
                onRetry = onRetryDetails
            )
        }
    }
}

@Composable
private fun MangaDetailsComponentPreview(
    mangaDetails: UiState<MangaDetailsUiModel>,
    characters: UiState<ImmutableList<MangaCharacterUiModel>>
) {
    AniMangaAnimeAndMangaInfoTheme {
        MangaDetailsComponent(
            isBookmarked = false,
            mangaDetails = mangaDetails,
            characters = characters,
            headerTitle = {},
            selectedImage = {},
            onRetryDetails = {},
            onRetryCharacters = {},
            onMoreCharactersClick = {},
            onClickBookmark = {},
            onClickShare = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailsComponentSuccessPreview() = MangaDetailsComponentPreview(
    mangaDetails = UiState.Success(MediaDetailsPreviewData.manga),
    characters = UiState.Success(MediaDetailsPreviewData.mangaCharacters)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailsComponentLoadingPreview() = MangaDetailsComponentPreview(
    mangaDetails = UiState.Loading,
    characters = UiState.Loading
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailsComponentErrorPreview() = MangaDetailsComponentPreview(
    mangaDetails = UiState.ErrorException(message = "Something went wrong. Please try again."),
    characters = UiState.Loading
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MangaDetailsComponentCharactersErrorPreview() = MangaDetailsComponentPreview(
    mangaDetails = UiState.Success(MediaDetailsPreviewData.manga),
    characters = UiState.ErrorException(message = "Couldn't load the characters.")
)
