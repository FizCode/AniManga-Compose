package dev.fizcode.mediadetails.presentation.people

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.mediadetails.presentation.MediaDetailsPreviewData
import dev.fizcode.mediadetails.presentation.info.component.CastShimmer
import dev.fizcode.mediadetails.presentation.info.component.CharacterCast
import dev.fizcode.mediadetails.presentation.info.component.CharacterRow
import dev.fizcode.mediadetails.presentation.info.component.StaffShimmer
import dev.fizcode.mediadetails.presentation.info.component.Staffs
import dev.fizcode.mediadetails.presentation.info.model.AnimeCastUiModel
import dev.fizcode.mediadetails.presentation.info.model.AnimeStaffUiModel
import dev.fizcode.mediadetails.presentation.info.model.MangaCharacterUiModel
import dev.fizcode.mediadetails.presentation.info.util.Constant as InfoConstant
import dev.fizcode.mediadetails.util.Constant
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/** All the characters and voice actors of an anime. */
@Composable
internal fun MediaCastScreen(
    mediaId: Int,
    onBackClick: () -> Unit
) {
    val viewModel: MediaCastViewModel = koinViewModel(key = "cast-$mediaId") {
        parametersOf(mediaId)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    CastListContent(
        state = state,
        onBackClick = onBackClick,
        onLoadMore = viewModel::loadMore,
        onRetry = viewModel::retry
    )
}

/** All the staff of an anime. */
@Composable
internal fun MediaStaffScreen(
    mediaId: Int,
    onBackClick: () -> Unit
) {
    val viewModel: MediaStaffViewModel = koinViewModel(key = "staff-$mediaId") {
        parametersOf(mediaId)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    StaffListContent(
        state = state,
        onBackClick = onBackClick,
        onLoadMore = viewModel::loadMore,
        onRetry = viewModel::retry
    )
}

/** All the characters of a manga. */
@Composable
internal fun MangaCharactersScreen(
    mediaId: Int,
    onBackClick: () -> Unit
) {
    val viewModel: MangaCharactersViewModel = koinViewModel(key = "characters-$mediaId") {
        parametersOf(mediaId)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterListContent(
        state = state,
        onBackClick = onBackClick,
        onLoadMore = viewModel::loadMore,
        onRetry = viewModel::retry
    )
}

@Composable
private fun CastListContent(
    state: UiState<PagedItems<AnimeCastUiModel>>,
    onBackClick: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit
) = PagedPeopleContent(
    title = InfoConstant.CHARACTERS_AND_VA,
    state = state,
    emptyMessage = Constant.EMPTY_CHARACTERS,
    onBackClick = onBackClick,
    onLoadMore = onLoadMore,
    onRetry = onRetry,
    shimmer = { CastShimmer() },
    itemContent = { cast -> CharacterCast(characterData = cast, onCastImageClick = {}) }
)

@Composable
private fun StaffListContent(
    state: UiState<PagedItems<AnimeStaffUiModel>>,
    onBackClick: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit
) = PagedPeopleContent(
    title = InfoConstant.STAFF,
    state = state,
    emptyMessage = Constant.EMPTY_STAFF,
    onBackClick = onBackClick,
    onLoadMore = onLoadMore,
    onRetry = onRetry,
    shimmer = { StaffShimmer() },
    itemContent = { staff -> Staffs(staffData = staff, onCastImageClick = {}) }
)

@Composable
private fun CharacterListContent(
    state: UiState<PagedItems<MangaCharacterUiModel>>,
    onBackClick: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit
) = PagedPeopleContent(
    title = InfoConstant.CHARACTERS,
    state = state,
    emptyMessage = Constant.EMPTY_CHARACTERS,
    onBackClick = onBackClick,
    onLoadMore = onLoadMore,
    onRetry = onRetry,
    shimmer = { StaffShimmer() },
    itemContent = { character -> CharacterRow(character = character, onClick = {}) }
)

private fun <T> sample(items: List<T>, repeat: Int = 4) =
    PagedItems(items = List(repeat) { items }.flatten().toImmutableList(), hasNextPage = true)

@Composable
private fun PeoplePreview(content: @Composable () -> Unit) {
    AniMangaAnimeAndMangaInfoTheme { content() }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastListPreview() = PeoplePreview {
    CastListContent(
        state = UiState.Success(sample(MediaDetailsPreviewData.animeCast)),
        onBackClick = {}, onLoadMore = {}, onRetry = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastListLoadingMorePreview() = PeoplePreview {
    CastListContent(
        state = UiState.Success(sample(MediaDetailsPreviewData.animeCast).copy(isLoadingMore = true)),
        onBackClick = {}, onLoadMore = {}, onRetry = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastListLoadMoreFailedPreview() = PeoplePreview {
    CastListContent(
        state = UiState.Success(sample(MediaDetailsPreviewData.animeCast).copy(loadMoreFailed = true)),
        onBackClick = {}, onLoadMore = {}, onRetry = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastListLoadingPreview() = PeoplePreview {
    CastListContent(state = UiState.Loading, onBackClick = {}, onLoadMore = {}, onRetry = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastListEmptyPreview() = PeoplePreview {
    CastListContent(state = UiState.Empty, onBackClick = {}, onLoadMore = {}, onRetry = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastListErrorPreview() = PeoplePreview {
    CastListContent(
        state = UiState.Error(message = "Something went wrong. Please try again."),
        onBackClick = {}, onLoadMore = {}, onRetry = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StaffListPreview() = PeoplePreview {
    StaffListContent(
        state = UiState.Success(sample(MediaDetailsPreviewData.animeStaff)),
        onBackClick = {}, onLoadMore = {}, onRetry = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CharacterListPreview() = PeoplePreview {
    CharacterListContent(
        state = UiState.Success(sample(MediaDetailsPreviewData.mangaCharacters)),
        onBackClick = {}, onLoadMore = {}, onRetry = {}
    )
}
