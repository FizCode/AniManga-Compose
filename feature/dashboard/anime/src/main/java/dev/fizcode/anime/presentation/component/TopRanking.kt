package dev.fizcode.anime.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.anime.presentation.model.TopRankingUiModel
import dev.fizcode.anime.presentation.model.dummyTopRankingUiModel
import dev.fizcode.common.base.responsehandler.UiState
import dev.fizcode.designsystem.component.card.MovieCardSmall
import dev.fizcode.designsystem.component.shimmer.MovieCardSmallShimmer

@Composable
internal fun TopRanking(
    headerTitle: String,
    cardItem: UiState<List<TopRankingUiModel>>,
    onHeaderClick: () -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    when (cardItem) {
        is UiState.Loading -> {
            AnimeContent(
                headerTitle = headerTitle,
                onHeaderClick = { }
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) {
                        MovieCardSmallShimmer()
                    }
                }
            }
        }

        is UiState.Success -> {
            AnimeContent(
                headerTitle = headerTitle,
                onHeaderClick = { onHeaderClick() }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cardItem.data.forEachIndexed { index, item ->
                        key(item.id) {
                            MovieCardSmall(
                                posterPath = item.posterPath,
                                rating = item.rating,
                                title = "${index + 1}. ${item.title}",
                                subTitle = item.subTitle,
                                studio = item.studio,
                                genre = item.genre,
                                onCardClick = { onCardClick(item.mediaType, item.id) }
                            )
                        }
                    }
                }
            }
        }

        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
private fun TopRankingPreview() {
    TopRanking(
        headerTitle = "Top Ranking 🏆",
        cardItem = UiState.Success(listOf(
            dummyTopRankingUiModel,
            dummyTopRankingUiModel,
            dummyTopRankingUiModel,
            dummyTopRankingUiModel,
        )),
        onHeaderClick = {},
        onCardClick = { _, _ -> }
    )
}
