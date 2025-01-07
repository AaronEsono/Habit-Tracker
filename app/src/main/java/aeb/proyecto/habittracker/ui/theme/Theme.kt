package aeb.proyecto.habittracker.ui.theme

import aeb.proyecto.habittracker.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Dark = darkColorScheme(
    background = backgroundDark,
    primary = primaryDark,
    onSurface = onSurfaceDark,
    inverseSurface = inverseSurfaceDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    surfaceVariant = surfaceVariantDark,
    secondaryContainer = secondaryContainerDark,
    tertiaryContainer = terciaryContainerDark,
    outline = outLineDark
)

private val Light = lightColorScheme(
    background = backgroundLight,
    primary = primaryLight,
    onSurface = onSurfaceLight,
    inverseSurface = inverseSurfaceLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    surfaceVariant = surfaceVariantLight,
    secondaryContainer = secondaryContainerLight,
    tertiaryContainer = terciaryContainerLight,
    outline = outLineLight
)

enum class AppTheme(val theme: Int, val text: Int) {
    DARK(0, R.string.settings_screen_card_dark),
    LIGHT(1, R.string.settings_screen_card_light)
}

@Composable
fun HabitTrackerTheme(
    theme:Int,
    content: @Composable () -> Unit
) {

    val themeScheme = when (theme) {
        AppTheme.DARK.theme -> Dark
        AppTheme.LIGHT.theme -> Light
        else -> Dark
    }

    MaterialTheme(
        colorScheme = themeScheme,
        typography = Typography,
        content = content
    )
}