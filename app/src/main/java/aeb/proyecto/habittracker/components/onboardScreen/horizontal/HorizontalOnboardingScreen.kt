package aeb.proyecto.habittracker.components.onboardScreen.horizontal

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.components.onboardScreen.components.button.OnboardingButton
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.ResultOptions
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.onboardingPages
import aeb.proyecto.habittracker.components.onboardScreen.components.pages.OnboardingPageScreen
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Displays the horizontal layout of the onboarding flow.
 *
 * The screen consists of the onboarding content, a skip action, and
 * navigation controls for moving between pages. The previous button
 * is hidden on the first page, while the next button is replaced by
 * a start button on the last page.
 *
 * Layout spacing is adjusted based on the available screen height
 * to provide a better experience on compact-height devices.
 *
 * @param pageSelected The onboarding page currently selected.
 * @param onClickResultOption Callback invoked when an onboarding action
 * is selected, such as skipping, navigating to the next or previous page,
 * or finishing the onboarding flow.
 */
@Composable
fun HorizontalOnboardingScreen(
    pageSelected: OnboardingPage,
    onClickResultOption: (ResultOptions) -> Unit
){

    val isFirstPage = pageSelected == onboardingPages.first()
    val isLastPage = pageSelected == onboardingPages.last()

    val configuration = LocalConfiguration.current
    val isCompactHeight = configuration.screenHeightDp < 400

    val contentPadding = if (isCompactHeight) spacing4 else spacing8
    val buttonSpacing = if (isCompactHeight) spacing2 else spacing4

    val previousAlpha by animateFloatAsState(
        targetValue = if (isFirstPage) 0f else 1f,
        animationSpec = tween(durationMillis = 220),
        label = "PreviousButtonAlphaAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .safeDrawingPadding()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.1f))

            OnboardingPageScreen(
                modifier = Modifier.weight(1f),
                pageSelected = pageSelected
            )

            Spacer(modifier = Modifier.weight(0.05f))
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(horizontal = contentPadding, vertical = spacing4),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            // Botón Omitir arriba a la derecha
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                OnboardingButton(
                    modifier = Modifier.padding(top = spacing4),
                    title = R.string.onboarding_skip,
                    onClick = { onClickResultOption(ResultOptions.Skip) }
                )
            }

            // Botones inferiores (Anterior / Siguiente)
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = spacing4)
            ) {
                OnboardingButton(
                    modifier = Modifier
                        .padding(end = buttonSpacing)
                        .alpha(previousAlpha),
                    title = R.string.onboarding_previous,
                    onClick = {
                        if (!isFirstPage) {
                            onClickResultOption(ResultOptions.Previous)
                        }
                    }
                )

                AnimatedContent(
                    targetState = isLastPage,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.92f))
                            .togetherWith(fadeOut(animationSpec = tween(90)))
                    },
                    label = "NextOrStartButtonAnimation"
                ) { targetIsLastPage ->
                    if (targetIsLastPage) {
                        OnboardingButton(
                            title = R.string.onboarding_start,
                            onClick = { onClickResultOption(ResultOptions.Finish) }
                        )
                    } else {
                        OnboardingButton(
                            title = R.string.onboarding_next,
                            onClick = { onClickResultOption(ResultOptions.Next) }
                        )
                    }
                }
            }
        }
    }
}