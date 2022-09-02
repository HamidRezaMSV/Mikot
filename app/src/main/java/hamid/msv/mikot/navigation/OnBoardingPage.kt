package hamid.msv.mikot.navigation

import androidx.annotation.DrawableRes
import hamid.msv.mikot.R

sealed class OnBoardingPage(@DrawableRes val image: Int, val description: Int) {
    object First : OnBoardingPage(
        image = R.drawable.welcome_1,
        description = R.string.fist_description
    )

    object Second : OnBoardingPage(
        image = R.drawable.welcome_2,
        description = R.string.second_description
    )

    object Third : OnBoardingPage(
        image = R.drawable.welcome_3,
        description = R.string.third_description
    )
}
