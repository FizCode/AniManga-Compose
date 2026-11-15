package dev.fizcode.onboarding.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.fizcode.designsystem.animation.AnimangaStudyIllustration
import dev.fizcode.designsystem.animation.AnimangaThankYouIllustration

@Composable
internal fun OnBoardingPagerScreen(
    onBoardingPage: OnBoardingPage,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val imageModifier = Modifier.size(240.dp)
        when (val image = onBoardingPage.image) {
            is OnBoardingImage.Drawable -> Image(
                modifier = imageModifier,
                painter = painterResource(id = image.id),
                contentDescription = null
            )

            OnBoardingImage.Study -> AnimangaStudyIllustration(modifier = imageModifier)
            OnBoardingImage.ThankYou -> AnimangaThankYouIllustration(modifier = imageModifier)
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = onBoardingPage.title
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            text = onBoardingPage.desc
        )
    }
}
