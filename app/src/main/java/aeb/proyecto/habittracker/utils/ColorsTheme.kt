package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.ui.theme.ThemeTextDark
import aeb.proyecto.habittracker.ui.theme.borderTextFieldDark
import aeb.proyecto.habittracker.ui.theme.colorBackgroundCardDark
import aeb.proyecto.habittracker.ui.theme.colorButtonsDark
import aeb.proyecto.habittracker.ui.theme.colorErrorGeneral
import aeb.proyecto.habittracker.ui.theme.colorStatusBarDark
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColorDark
import aeb.proyecto.habittracker.ui.theme.primaryColorAppDark
import aeb.proyecto.habittracker.ui.theme.secondaryColorAppDark
import aeb.proyecto.habittracker.ui.theme.terciaryColorAppDark
import aeb.proyecto.habittracker.ui.theme.textColorsDark
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ColorsTheme {
    var primaryColorApp by mutableStateOf(primaryColorAppDark)
    var secondaryColorApp by mutableStateOf(secondaryColorAppDark)
    var terciaryColorApp by mutableStateOf(terciaryColorAppDark)

    var colorButtons by mutableStateOf(colorButtonsDark)
    var borderTextField by mutableStateOf(borderTextFieldDark)
    var containerTextFieldColor by mutableStateOf(containerTextFieldColorDark)

    var themeText by mutableStateOf(ThemeTextDark)
    var textColors by mutableStateOf(textColorsDark)
    var colorError by mutableStateOf(colorErrorGeneral)

    var colorBackgroundCard by mutableStateOf(colorBackgroundCardDark)
    var colorStatusBar by mutableStateOf(colorStatusBarDark)
}