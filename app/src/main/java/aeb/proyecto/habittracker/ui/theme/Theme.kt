package aeb.proyecto.habittracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Dark = darkColorScheme(
    background = backgroundDark,
    primary = primaryDark,
    onSurface = Color.White,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark
)

private val Light = lightColorScheme(
    background = backgroundLight,
    primary = primaryLight,
    onSurface = Color.Black,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight
)

enum class AppTheme(val theme: Int) {
    DARK(0), LIGHT(1)
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