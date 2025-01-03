package aeb.proyecto.habittracker.ui.theme

import aeb.proyecto.habittracker.utils.ColorsTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ColorsTheme.primaryColorApp,
    secondary = ColorsTheme.secondaryColorApp,
    tertiary = ColorsTheme.terciaryColorApp
)

private val LightColorScheme = lightColorScheme(
    primary = ColorsTheme.primaryColorApp,
    secondary = ColorsTheme.secondaryColorApp,
    tertiary = ColorsTheme.terciaryColorApp

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun HabitTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}