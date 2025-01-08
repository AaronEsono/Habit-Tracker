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
    tertiaryContainer = tertiaryContainerDark,
    outline = outLineDark,
    error = colorErrorGeneral,
    surfaceContainer = surfaceContainerDark,
    surfaceTint = surfaceTintDark,
    surfaceContainerHighest = surfaceContainerHighestDark
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
    tertiaryContainer = tertiaryContainerLight,
    outline = outLineLight,
    error = colorErrorGeneral,
    surfaceContainer = surfaceContainerLight,
    surfaceTint = surfaceTintLight,
    surfaceContainerHighest = surfaceContainerHighestLight
)

private val DarkAlt = darkColorScheme(
    background = backgroundDarkAlt,
    primary = primaryDarkAlt,
    onSurface = onSurfaceDarkAlt,
    inverseSurface = inverseSurfaceDarkAlt,
    primaryContainer = primaryContainerDarkAlt,
    onPrimaryContainer = onPrimaryContainerDarkAlt,
    surfaceVariant = surfaceVariantDarkAlt,
    secondaryContainer = secondaryContainerDarkAlt,
    tertiaryContainer = tertiaryContainerDarkAlt,
    outline = outLineDarkAlt,
    error = colorErrorGeneral,
    surfaceContainer = surfaceContainerDarkAlt,
    surfaceTint = surfaceTintDarkAlt,
    surfaceContainerHighest = surfaceContainerHighestDarkAlt
)

private val LightAlt = lightColorScheme(
    background = backgroundLightAlt,
    primary = primaryLightAlt,
    onSurface = onSurfaceLightAlt,
    inverseSurface = inverseSurfaceLightAlt,
    primaryContainer = primaryContainerLightAlt,
    onPrimaryContainer = onPrimaryContainerLightAlt,
    surfaceVariant = surfaceVariantLightAlt,
    secondaryContainer = secondaryContainerLightAlt,
    tertiaryContainer = tertiaryContainerLightAlt,
    outline = outLineLightAlt,
    error = colorErrorGeneral,
    surfaceContainer = surfaceContainerLightAlt,
    surfaceTint = surfaceTintLightAlt,
    surfaceContainerHighest = surfaceContainerHighestLightAlt
)

enum class AppTheme(val theme: Int, val text: Int) {
    DARK(0, R.string.settings_screen_card_dark),
    BLUE_DARK(1, R.string.settings_screen_card_dark_alt),
    LIGHT(2, R.string.settings_screen_card_light),
    CREME_LIGHT(3, R.string.settings_screen_card_light_alt)
}

@Composable
fun HabitTrackerTheme(
    theme:Int,
    content: @Composable () -> Unit
) {

    val themeScheme = when (theme) {
        AppTheme.DARK.theme -> Dark
        AppTheme.LIGHT.theme -> Light
        AppTheme.BLUE_DARK.theme -> DarkAlt
        AppTheme.CREME_LIGHT.theme -> LightAlt
        else -> Dark
    }

    MaterialTheme(
        colorScheme = themeScheme,
        typography = Typography,
        content = content
    )
}