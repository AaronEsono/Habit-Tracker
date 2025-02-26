package aeb.proyecto.habittracker.ui.theme

import aeb.proyecto.habittracker.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppTheme(val theme: Int, val text: Int) {
    DARK(0, R.string.settings_screen_card_dark),
    BLUE_DARK(1, R.string.settings_screen_card_dark_alt),
    LIGHT(2, R.string.settings_screen_card_light),
    CREME_LIGHT(3, R.string.settings_screen_card_light_alt)
}