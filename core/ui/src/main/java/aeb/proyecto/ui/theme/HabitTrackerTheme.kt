package aeb.proyecto.ui.theme

import aeb.proyecto.ui.typography.Typography
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Root architectural theme director governing the application's global styling environment.
 * Evaluates the active persistent theme index to inject the corresponding multi-palette [ColorScheme]
 * and unifies it alongside the core system [Typography] specs down the composition tree.
 *
 * Acts as the centralized design system gateway, ensuring that all down-stream atom tokens
 * adapt dynamically to light, dark, monochromatic, or sub-toned stylistic mutations.
 *
 * @param theme The persistent integer identifier token reflecting the user's active theme profile choice.
 * @param content The declarative visual view hierarchy layout nested inside the theme's environmental scope.
 */
@Composable
fun HabitTrackerTheme(
    theme:Int,
    content: @Composable () -> Unit
) {

    // Resolve the static ColorScheme matrix token synchronously based on the persistent storage index
    val themeScheme = when (theme) {
        EnumTheme.DARK.theme -> DarkTheme
        EnumTheme.MIDNIGHT.theme -> MidnightTheme
        EnumTheme.SLATE_MOSS.theme -> SlateMossTheme
        EnumTheme.PLUM.theme -> PlumTheme
        EnumTheme.SOFT_STONE.theme -> SoftStoneTheme
        EnumTheme.ICE_MINT.theme -> IceMintTheme
        EnumTheme.DESERT_ROSE.theme -> DesertRoseTheme
        EnumTheme.CYBER_DENIM.theme -> CyberDenimTheme
        // Defensive fallback boundary to secure layout continuity against corrupt database variables
        else -> DarkTheme
    }

    // Encapsulate the application layout structure within the scoped Material Design 3 local configuration provider
    MaterialTheme(
        colorScheme = themeScheme,
        typography = Typography,
        content = content
    )
}