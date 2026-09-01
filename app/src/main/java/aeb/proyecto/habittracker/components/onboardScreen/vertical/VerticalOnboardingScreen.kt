package aeb.proyecto.habittracker.components.onboardScreen.vertical

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.components.onboardScreen.components.button.OnboardingButton
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.ResultOptions
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.onboardingPages
import aeb.proyecto.habittracker.components.onboardScreen.components.pages.OnboardingPageScreen
import aeb.proyecto.habittracker.components.onboardScreen.components.pages.PageIndicator
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * Displays the vertical layout of the onboarding flow.
 *
 * The screen consists of a skip action at the top, the currently selected
 * onboarding page, and navigation controls at the bottom. The previous
 * button is displayed only when the first page is not selected, while the
 * next button is replaced by a start button on the last page.
 *
 * @param pageSelected The onboarding page currently selected.
 * @param onClickResultOption Callback invoked when an onboarding action
 * is selected, such as skipping, navigating to the next or previous page,
 * or finishing the onboarding flow.
 */
@Composable
fun VerticalOnboardingScreen(
    pageSelected: OnboardingPage,
    onClickResultOption: (ResultOptions) -> Unit
){

    val isFirstPage = pageSelected == onboardingPages.first()
    val isLastPage = pageSelected == onboardingPages.last()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .safeDrawingPadding()
    ) {

        //Skip button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OnboardingButton(
                modifier = Modifier
                    .padding(end = spacing8, top = spacing4, bottom = spacing4),
                title = R.string.onboarding_skip,
                onClick = { onClickResultOption(ResultOptions.Skip) }
            )
        }

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        //Pages
        OnboardingPageScreen(
            modifier = Modifier.weight(1f),
            pageSelected = pageSelected
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing8, vertical = spacing4),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = !isFirstPage,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it / 2 }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it / 2 })
            ) {
                OnboardingButton(
                    modifier = Modifier.padding(end = spacing4),
                    title = R.string.onboarding_previous,
                    onClick = { onClickResultOption(ResultOptions.Previous) }
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = spacing4))

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