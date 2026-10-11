package dev.fizcode.onboarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.button.SimpleTextButton
import dev.fizcode.designsystem.component.pager.PagerPositionDotIndicator
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.onboarding.presentation.component.OnBoardingPage
import dev.fizcode.onboarding.presentation.component.OnBoardingPagerScreen
import dev.fizcode.onboarding.presentation.util.Constant
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Introduction pages shown on first launch.
 *
 * Skipping it or finishing its last page remembers that it was seen, so it only shows once.
 *
 * @param onFinished called once the onboarding is saved as seen and the screen can be left.
 */
@Composable
internal fun OnBoardingScreen(
    onFinished: () -> Unit
) {
    val viewModel: OnBoardingViewModel = koinViewModel()
    val scope = rememberCoroutineScope()

    OnBoardingScreenContent(
        pages = OnBoardingPage.all,
        onClickSkip = {
            scope.launch {
                viewModel.completeOnBoarding()
                onFinished()
            }
        }
    )
}

@Composable
private fun OnBoardingScreenContent(
    pages: List<OnBoardingPage>,
    onClickSkip: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    val currentPage = pagerState.currentPage
    val isFirstPage = currentPage == 0
    val isLastPage = currentPage == pages.lastIndex

    fun scrollTo(page: Int) {
        scope.launch { pagerState.scrollToPage(page) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState
        ) { position ->
            OnBoardingPagerScreen(onBoardingPage = pages[position])
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PagerPositionDotIndicator(pagerState = pagerState)
        }

        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Goes to the next page, on the last page it finishes the onboarding.
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = if (isLastPage) onClickSkip else { { scrollTo(currentPage + 1) } }
            ) {
                Text(text = if (isLastPage) Constant.CATCH_THE_ANIME else Constant.NEXT)
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isFirstPage) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    SimpleTextButton(
                        text = Constant.PREVIOUS,
                        onClickButton = { scrollTo(currentPage - 1) }
                    )
                }

                // Nothing left to skip on the last page.
                if (isLastPage) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    SimpleTextButton(text = Constant.SKIP, onClickButton = onClickSkip)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnBoardingScreenPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        OnBoardingScreenContent(pages = OnBoardingPage.all, onClickSkip = {})
    }
}
