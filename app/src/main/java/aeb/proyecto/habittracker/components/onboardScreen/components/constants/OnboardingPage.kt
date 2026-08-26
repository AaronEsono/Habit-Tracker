package aeb.proyecto.habittracker.components.onboardScreen.components.constants
import aeb.proyecto.habittracker.R

sealed class OnboardingPage(
    val title: Int,
    val subtitle: Int,
    val image: Int
) {
    data object First : OnboardingPage(
        title = R.string.onboarding_page_1_title,
        subtitle = R.string.onboarding_page_1_subtitle,
        image = R.drawable.im_page_1
    )
    data object Second : OnboardingPage(
        title = R.string.onboarding_page_2_title,
        subtitle = R.string.onboarding_page_2_subtitle,
        image = R.drawable.im_page_2
    )

    data object Third : OnboardingPage(
        title = R.string.onboarding_page_3_title,
        subtitle = R.string.onboarding_page_3_subtitle,
        image = R.drawable.im_page_3
    )
    data object Fourth : OnboardingPage(
        title = R.string.onboarding_page_4_title,
        subtitle = R.string.onboarding_page_4_subtitle,
        image = R.drawable.im_page_4
    )

    data object Fifth : OnboardingPage(
        title = R.string.onboarding_page_5_title,
        subtitle = R.string.onboarding_page_5_subtitle,
        image = R.drawable.im_page_5
    )
}

val onboardingPages: List<OnboardingPage> = listOf(
    OnboardingPage.First,
    OnboardingPage.Second,
    OnboardingPage.Third,
    OnboardingPage.Fourth,
    OnboardingPage.Fifth
)