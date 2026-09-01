package aeb.proyecto.habittracker.components.onboardScreen.components.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

/**
 * Displays a text-based button used for onboarding actions.
 *
 * The button adjusts its text size based on the available screen width
 * to provide a better experience on smaller devices.
 *
 * @param modifier Modifier used to customize the button's layout and appearance.
 * @param title String resource ID used as the button's text.
 * @param onClick Callback invoked when the button is clicked.
 */
@Composable
fun OnboardingButton(
    modifier: Modifier = Modifier,
    title:Int,
    onClick: () -> Unit,
){

    val configuration = LocalConfiguration.current
    val isSmallScreen = configuration.screenWidthDp < 360

    val buttonTextSize = if (isSmallScreen) 16.sp else 20.sp

    TextButton(
        modifier = modifier
            .testTag("onboarding_button"),
        onClick = onClick
    ) {
        TitleSmallText(
            text = stringResource(title),
            fontSize = buttonTextSize,
        )
    }

}