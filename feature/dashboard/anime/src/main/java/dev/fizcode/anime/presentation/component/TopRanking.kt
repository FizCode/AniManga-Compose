package dev.fizcode.anime.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    AnimeContent(
        headerTitle = headerTitle,
        onHeaderClick = { onHeaderClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (cardItem) {
                is UiState.Success -> {
                    cardItem.data.forEachIndexed { index, item ->
                        MovieCardSmall(
                            posterPath = item.posterPath,
                            rating = item.rating,
                            title = "${index+1}. ${item.title}",
                            subTitle = item.subTitle,
                            studio = item.studio,
                            genre = item.genre,
                            onCardClick = { onCardClick(item.mediaType, item.id) }
                        )
                    }
                }

                else -> {
                    repeat(times = 5) {
                        MovieCardSmallShimmer()
                    }
                }

            }
        }
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
