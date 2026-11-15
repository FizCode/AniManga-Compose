package dev.fizcode.onboarding.presentation.component

import androidx.annotation.DrawableRes
import dev.fizcode.onboarding.presentation.util.Constant.OnBoardingContents

internal sealed interface OnBoardingImage {
    data class Drawable(@DrawableRes val id: Int) : OnBoardingImage
    data object Study : OnBoardingImage
    data object ThankYou : OnBoardingImage
}

/** A single onboarding page. [all] lists the pages in the order they are shown. */
internal sealed class OnBoardingPage(
    val image: OnBoardingImage,
    val title: String,
    val desc: String
) {
    data object First : OnBoardingPage(
        image = OnBoardingImage.Study,
        title = OnBoardingContents.TITLE_PAGE_1,
        desc = OnBoardingContents.DESC_PAGE_1
    )

    data object Second : OnBoardingPage(
        image = OnBoardingImage.Drawable(OnBoardingContents.IMG_PAGE_2),
        title = OnBoardingContents.TITLE_PAGE_2,
        desc = OnBoardingContents.DESC_PAGE_2
    )

    data object Third : OnBoardingPage(
        image = OnBoardingImage.Drawable(OnBoardingContents.IMG_PAGE_3),
        title = OnBoardingContents.TITLE_PAGE_3,
        desc = OnBoardingContents.DESC_PAGE_3
    )

    data object Fourth : OnBoardingPage(
        image = OnBoardingImage.ThankYou,
        title = OnBoardingContents.TITLE_PAGE_4,
        desc = OnBoardingContents.DESC_PAGE_4
    )

    companion object {
        val all: List<OnBoardingPage> = listOf(First, Second, Third, Fourth)
    }
}
