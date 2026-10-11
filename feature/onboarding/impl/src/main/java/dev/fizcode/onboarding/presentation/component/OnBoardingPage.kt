package dev.fizcode.onboarding.presentation.component

import androidx.annotation.DrawableRes
import dev.fizcode.onboarding.presentation.util.Constant.OnBoardingContents

/** A single onboarding page. [all] lists the pages in the order they are shown. */
internal sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val desc: String
) {
    data object First : OnBoardingPage(
        image = OnBoardingContents.IMG_PAGE_1,
        title = OnBoardingContents.TITLE_PAGE_1,
        desc = OnBoardingContents.DESC_PAGE_1
    )

    data object Second : OnBoardingPage(
        image = OnBoardingContents.IMG_PAGE_2,
        title = OnBoardingContents.TITLE_PAGE_2,
        desc = OnBoardingContents.DESC_PAGE_2
    )

    data object Third : OnBoardingPage(
        image = OnBoardingContents.IMG_PAGE_3,
        title = OnBoardingContents.TITLE_PAGE_3,
        desc = OnBoardingContents.DESC_PAGE_3
    )

    data object Fourth : OnBoardingPage(
        image = OnBoardingContents.IMG_PAGE_4,
        title = OnBoardingContents.TITLE_PAGE_4,
        desc = OnBoardingContents.DESC_PAGE_4
    )

    companion object {
        val all: List<OnBoardingPage> = listOf(First, Second, Third, Fourth)
    }
}
