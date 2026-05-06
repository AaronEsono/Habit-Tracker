package aeb.proyecto.ui.theme

import aeb.proyecto.ui.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import aeb.proyecto.ui.typography.Typography

@Composable
fun HabitTrackerTheme(
    theme:Int,
    content: @Composable () -> Unit
) {

    val themeScheme = when (theme) {
        EnumTheme.DARK.theme -> Dark
        EnumTheme.LIGHT.theme -> Light
        EnumTheme.BLUE_DARK.theme -> DarkAlt
        EnumTheme.CREME_LIGHT.theme -> LightAlt
        EnumTheme.MIDNIGHT.theme -> midnightDark
        else -> Dark
    }

    MaterialTheme(
        colorScheme = themeScheme,
        typography = Typography,
        content = content
    )
}