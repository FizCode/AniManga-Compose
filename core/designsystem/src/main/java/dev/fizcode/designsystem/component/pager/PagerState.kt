package dev.fizcode.designsystem.component.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Row of dots for a pager; the dot of the current page uses the primary color, the others
 * the primary container color. Place it inside a layout such as a [Row].
 *
 * @param pagerState state of the pager to mirror. The page count is read once and remembered.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerPositionDotIndicator(
    pagerState: PagerState
) {
    val pageSize = remember { pagerState.pageCount }

    repeat(pageSize) { iteration ->
        if (pagerState.currentPage == iteration) {
            DotIndicator(dotColor = MaterialTheme.colorScheme.primary)
        } else {
            DotIndicator(dotColor = MaterialTheme.colorScheme.primaryContainer)
        }
    }
}

@Composable
private fun DotIndicator(
    dotColor: Color
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape)
            .background(dotColor)
            .size(8.dp)
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun PagerPositionDotIndicatorPreview() {
    Row {
        PagerPositionDotIndicator(pagerState = rememberPagerState(initialPage = 1) { 4 })
    }
}
