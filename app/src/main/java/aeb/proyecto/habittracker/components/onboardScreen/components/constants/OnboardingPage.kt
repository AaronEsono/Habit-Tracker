package aeb.proyecto.habittracker.components.onboardScreen.components.constants
import aeb.proyecto.habittracker.R

/**
 * Represents a page in the onboarding flow.
 *
 * Each page contains the resources required to display its title,
 * subtitle, and illustration.
 *
 * @property title String resource ID for the page title.
 * @property subtitle String resource ID for the page subtitle.
 * @property image Drawable resource ID for the page illustration.
 */
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

/**
 * Ordered list of all pages available in the onboarding flow.
 *
 * The order of this list determines the navigation sequence
 * when moving between onboarding pages.
 */
val onboardingPages: List<OnboardingPage> = listOf(
    OnboardingPage.First,
    OnboardingPage.Second,
    OnboardingPage.Third,
    OnboardingPage.Fourth,
    OnboardingPage.Fifth
)