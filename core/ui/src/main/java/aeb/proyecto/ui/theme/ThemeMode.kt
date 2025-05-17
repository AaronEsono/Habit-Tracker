package aeb.proyecto.ui.theme

import aeb.proyecto.ui.color.backgroundDark
import aeb.proyecto.ui.color.backgroundDarkAlt
import aeb.proyecto.ui.color.backgroundLight
import aeb.proyecto.ui.color.backgroundLightAlt
import aeb.proyecto.ui.color.colorErrorGeneral
import aeb.proyecto.ui.color.inverseSurfaceDark
import aeb.proyecto.ui.color.inverseSurfaceDarkAlt
import aeb.proyecto.ui.color.inverseSurfaceLight
import aeb.proyecto.ui.color.inverseSurfaceLightAlt
import aeb.proyecto.ui.color.onBackgroundDark
import aeb.proyecto.ui.color.onPrimaryContainerDark
import aeb.proyecto.ui.color.onPrimaryContainerDarkAlt
import aeb.proyecto.ui.color.onPrimaryContainerLight
import aeb.proyecto.ui.color.onPrimaryContainerLightAlt
import aeb.proyecto.ui.color.onSurfaceDark
import aeb.proyecto.ui.color.onSurfaceDarkAlt
import aeb.proyecto.ui.color.onSurfaceLight
import aeb.proyecto.ui.color.onSurfaceLightAlt
import aeb.proyecto.ui.color.onSurfaceVariantDark
import aeb.proyecto.ui.color.outLineDark
import aeb.proyecto.ui.color.outLineDarkAlt
import aeb.proyecto.ui.color.outLineLight
import aeb.proyecto.ui.color.outLineLightAlt
import aeb.proyecto.ui.color.primaryContainerDark
import aeb.proyecto.ui.color.primaryContainerDarkAlt
import aeb.proyecto.ui.color.primaryContainerLight
import aeb.proyecto.ui.color.primaryContainerLightAlt
import aeb.proyecto.ui.color.primaryDark
import aeb.proyecto.ui.color.primaryDarkAlt
import aeb.proyecto.ui.color.primaryLight
import aeb.proyecto.ui.color.primaryLightAlt
import aeb.proyecto.ui.color.scrimDark
import aeb.proyecto.ui.color.secondaryContainerDark
import aeb.proyecto.ui.color.secondaryContainerDarkAlt
import aeb.proyecto.ui.color.secondaryContainerLight
import aeb.proyecto.ui.color.secondaryContainerLightAlt
import aeb.proyecto.ui.color.surfaceContainerDark
import aeb.proyecto.ui.color.surfaceContainerDarkAlt
import aeb.proyecto.ui.color.surfaceContainerHighestDark
import aeb.proyecto.ui.color.surfaceContainerHighestDarkAlt
import aeb.proyecto.ui.color.surfaceContainerHighestLight
import aeb.proyecto.ui.color.surfaceContainerHighestLightAlt
import aeb.proyecto.ui.color.surfaceContainerLight
import aeb.proyecto.ui.color.surfaceContainerLightAlt
import aeb.proyecto.ui.color.surfaceContainerLowDark
import aeb.proyecto.ui.color.surfaceContainerLowestDark
import aeb.proyecto.ui.color.surfaceTintDark
import aeb.proyecto.ui.color.surfaceTintDarkAlt
import aeb.proyecto.ui.color.surfaceTintLight
import aeb.proyecto.ui.color.surfaceTintLightAlt
import aeb.proyecto.ui.color.surfaceVariantDark
import aeb.proyecto.ui.color.surfaceVariantDarkAlt
import aeb.proyecto.ui.color.surfaceVariantLight
import aeb.proyecto.ui.color.surfaceVariantLightAlt
import aeb.proyecto.ui.color.tertiaryContainerDark
import aeb.proyecto.ui.color.tertiaryContainerDarkAlt
import aeb.proyecto.ui.color.tertiaryContainerLight
import aeb.proyecto.ui.color.tertiaryContainerLightAlt
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Dark = darkColorScheme(
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
    surfaceContainerHighest = surfaceContainerHighestDark,
    scrim = scrimDark,
    onBackground = onBackgroundDark,
    onSurfaceVariant = onSurfaceVariantDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainerLowest = surfaceContainerLowestDark
)

val Light = lightColorScheme(
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
    surfaceContainerHighest = surfaceContainerHighestLight,
    scrim = Color.Gray
)

val DarkAlt = darkColorScheme(
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

val LightAlt = lightColorScheme(
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