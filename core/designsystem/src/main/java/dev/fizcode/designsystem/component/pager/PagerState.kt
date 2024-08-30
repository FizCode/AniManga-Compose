package dev.fizcode.designsystem.component.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
