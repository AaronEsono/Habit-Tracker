package aeb.proyecto.habittracker.components.onboardScreen

import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.ResultOptions
import aeb.proyecto.habittracker.components.onboardScreen.horizontal.HorizontalOnboardingScreen
import aeb.proyecto.habittracker.components.onboardScreen.vertical.VerticalOnboardingScreen
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import androidx.compose.runtime.Composable

/**
 * Displays the onboarding screen using a layout adapted to the current
 * device orientation.
 *
 * The vertical layout is used in portrait mode, while the horizontal
 * layout is used in landscape mode.
 *
 * @param pageSelected The onboarding page currently selected.
 * @param onClickResultOption Callback invoked when an onboarding action
 * is selected, such as skipping, navigating to the next or previous page,
 * or finishing the onboarding flow.
 */
@Composable
fun OnboardScreen(
    pageSelected: OnboardingPage,
    onClickResultOption: (ResultOptions) -> Unit
){
    val orientation = getOrientation()

    when(orientation){
        Orientation.Portrait -> {
            VerticalOnboardingScreen(
                pageSelected = pageSelected,
                onClickResultOption = onClickResultOption
            )
        }
        Orientation.Landscape -> {
            HorizontalOnboardingScreen(
                pageSelected = pageSelected,
                onClickResultOption = onClickResultOption
            )
        }
    }

}