package dev.fizcode.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.component.button.SimpleTextButton
import dev.fizcode.designsystem.component.pager.PagerPositionDotIndicator
import dev.fizcode.onboarding.presentation.component.OnBoardingPage
import dev.fizcode.onboarding.presentation.component.OnBoardingPagerScreen
import dev.fizcode.onboarding.presentation.util.Constant
import kotlinx.coroutines.launch

@Composable
fun OnBoardingRoute() {
    OnBoardingScreen(
        onClickSkip = {}
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnBoardingScreen(
    onClickSkip: () -> Unit
) {

    val page = remember {
        listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)
    }

    val pagerState = rememberPagerState(pageCount = { page.size })
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    val scope = rememberCoroutineScope()
    val onPreviousClick = remember(scope, currentPage) { {
            if (currentPage > 0) {
                scope.launch {
                    pagerState.scrollToPage(currentPage - 1)
                }
            }
        } }

    val onNextClick = remember(scope, currentPage) { {
        if (currentPage < 3) {
            scope.launch {
                pagerState.scrollToPage(currentPage + 1)
            }
        }
    } }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState
        ) { position ->
            // Pager Screen
            OnBoardingPagerScreen(onBoardingPage = page[position])
        }

        // Dot Indicator showing Pager position
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PagerPositionDotIndicator(pagerState = pagerState)
        }
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            // Navigate to Sign In and set onboarding as already opened
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onClickSkip
            ) {
                Text(text = Constant.SKIP)
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Scroll to the previous pager position
                if (currentPage != 0) {
                    SimpleTextButton(text = Constant.PREVIOUS, onClickButton = onPreviousClick)
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Scroll to the next pager position
                if (currentPage == 2) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    SimpleTextButton(text = Constant.NEXT, onClickButton = onNextClick)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingPreview() {
    OnBoardingScreen(
        onClickSkip = {}
    )
}
