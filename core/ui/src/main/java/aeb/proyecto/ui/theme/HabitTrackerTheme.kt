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
        EnumTheme.MIDNIGHT.theme -> midnightDark
        EnumTheme.SLATE_MOSS.theme -> SlateMoss
        EnumTheme.PLUM.theme -> Plum
        EnumTheme.LIGHT.theme -> Light
        EnumTheme.CREME_LIGHT.theme -> LightAlt
        EnumTheme.SOFT_STONE.theme -> SoftStone
        else -> Dark
    }

    MaterialTheme(
        colorScheme = themeScheme,
        typography = Typography,
        content = content
    )
}