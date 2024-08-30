package dev.fizcode.onboarding.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import dev.fizcode.onboarding.presentation.util.Constant
import dev.fizcode.onboarding.presentation.util.ImageConstant

@Immutable
internal sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val desc: String
) {
    data object First : OnBoardingPage(
        image = ImageConstant.img_page_1,
        title = Constant.OnBoardingContents.TITLE_PAGE_1,
        desc = Constant.OnBoardingContents.DESC_PAGE_1
    )

    data object Second : OnBoardingPage(
        image = ImageConstant.img_page_2,
        title = Constant.OnBoardingContents.TITLE_PAGE_2,
        desc = Constant.OnBoardingContents.DESC_PAGE_2
    )

    data object Third : OnBoardingPage(
        image = ImageConstant.img_page_3,
        title = Constant.OnBoardingContents.TITLE_PAGE_3,
        desc = Constant.OnBoardingContents.DESC_PAGE_3
    )
}
