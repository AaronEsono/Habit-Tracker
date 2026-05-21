package aeb.proyecto.ui.theme

import aeb.proyecto.ui.typography.Typography
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun HabitTrackerTheme(
    theme:Int,
    content: @Composable () -> Unit
) {

    val themeScheme = when (theme) {
        EnumTheme.DARK.theme -> DarkTheme
        EnumTheme.MIDNIGHT.theme -> MidnightTheme
        EnumTheme.SLATE_MOSS.theme -> SlateMossTheme
        EnumTheme.PLUM.theme -> PlumTheme
        EnumTheme.SOFT_STONE.theme -> SoftStoneTheme
        EnumTheme.ICE_MINT.theme -> IceMintTheme
        EnumTheme.DESERT_ROSE.theme -> DesertRoseTheme
        EnumTheme.CYBER_DENIM.theme -> CyberDenimTheme
        else -> DarkTheme
    }

    MaterialTheme(
        colorScheme = themeScheme,
        typography = Typography,
        content = content
    )
}