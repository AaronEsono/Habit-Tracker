package aeb.proyecto.habittracker.components.onboardScreen.components.pages

import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag

/**
 * Displays a horizontal indicator representing the current position
 * within a collection of pages.
 *
 * The selected indicator expands and changes color, while unselected
 * indicators remain smaller and use the unselected color. Indicator
 * dimensions are adjusted based on the screen width to improve the
 * layout on smaller devices.
 *
 * @param pageCount Total number of pages represented by the indicator.
 * @param currentPageIndex Zero-based index of the currently selected page.
 * @param modifier Modifier used to customize the indicator's layout
 * and appearance.
 * @param selectedColor Color used for the currently selected indicator.
 * Defaults to the theme's `onSurface` color.
 * @param unselectedColor Color used for indicators that are not selected.
 * Defaults to the theme's `scrim` color.
 */
@Composable
fun PageIndicator(
    pageCount: Int,
    currentPageIndex: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.onSurface,
    unselectedColor: Color = MaterialTheme.colorScheme.scrim
) {
    val configuration = LocalConfiguration.current
    val isSmallScreen = configuration.screenWidthDp < 360

    val selectedWidthTarget = if (isSmallScreen) spacing16 else spacing24
    val unselectedWidthTarget = if (isSmallScreen) spacing6 else spacing8
    val dotHeight = if (isSmallScreen) spacing6 else spacing8
    val dotSpacing = if (isSmallScreen) spacing6 else spacing8

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dotSpacing, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPageIndex

            val width by animateDpAsState(
                targetValue = if (isSelected) selectedWidthTarget else unselectedWidthTarget,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "indicatorWidth"
            )

            val color by animateColorAsState(
                targetValue = if (isSelected) selectedColor else unselectedColor,
                animationSpec = tween(durationMillis = 300),
                label = "indicatorColor"
            )

            Box(
                modifier = Modifier
                    .height(dotHeight)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
                    .testTag("onboarding_indicator_${index}")
            )
        }
    }
}