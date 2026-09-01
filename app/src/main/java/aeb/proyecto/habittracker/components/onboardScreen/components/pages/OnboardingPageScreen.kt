package aeb.proyecto.habittracker.components.onboardScreen.components.pages

import aeb.proyecto.attributions.R
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.onboardingPages
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing18
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Displays the content of the currently selected onboarding page.
 *
 * The page image and text content are animated when navigating between pages.
 * The animation direction is determined by the position of the target page
 * relative to the currently displayed page. The page indicator remains static
 * while its individual indicators animate according to the selected page.
 *
 * Text sizes are adjusted based on the screen width to improve readability
 * on smaller devices.
 *
 * @param modifier Modifier used to customize the layout and appearance.
 * @param pageSelected The onboarding page currently selected and displayed.
 */
@Composable
fun OnboardingPageScreen(
    modifier: Modifier = Modifier,
    pageSelected: OnboardingPage
){

    val configuration = LocalConfiguration.current
    val isSmallScreen = configuration.screenWidthDp < 360

    val titleSize = if (isSmallScreen) 18.sp else 24.sp
    val subtitleSize = if (isSmallScreen) 13.sp else 16.sp
    val currentPageIndex = onboardingPages.indexOf(pageSelected)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = pageSelected,
            transitionSpec = {
                val targetIndex = onboardingPages.indexOf(targetState)
                val initialIndex = onboardingPages.indexOf(initialState)
                val isForward = targetIndex > initialIndex

                val slideIn = slideInHorizontally(
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
                    initialOffsetX = { fullWidth -> if (isForward) fullWidth / 3 else -fullWidth / 3 }
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 500)
                ) + scaleIn(
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
                    initialScale = 0.92f
                )

                val slideOut = slideOutHorizontally(
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                    targetOffsetX = { fullWidth -> if (isForward) -fullWidth / 3 else fullWidth / 3 }
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                ) + scaleOut(
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                    targetScale = 0.95f
                )

                slideIn togetherWith slideOut
            },
            modifier = Modifier.weight(1f),
            label = "OnboardingPageAnimation"
        ) { targetPage ->

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = targetPage.image),
                        contentDescription = "Onboarding image for ${targetPage.javaClass.simpleName}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("onboarding_image")
                    )
                }

                Spacer(modifier = Modifier.height(spacing12))

                TitleMediumText(
                    text = stringResource(targetPage.title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().testTag("onboarding_title"),
                    fontWeight = FontWeight.Bold,
                    fontSize = titleSize
                )

                Spacer(modifier = Modifier.height(spacing6))

                LabelLargeText(
                    text = stringResource(targetPage.subtitle),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().testTag("onboarding_subtitle"),
                    fontSize = subtitleSize
                )

                Spacer(modifier = Modifier.weight(0.4f))
            }
        }

        // El indicador queda FUERA del AnimatedContent: no se desliza, solo anima sus dots
        Spacer(modifier = Modifier.height(spacing16))

        PageIndicator(
            pageCount = onboardingPages.size,
            currentPageIndex = currentPageIndex,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(spacing16))
    }
}