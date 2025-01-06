package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.ui.theme.ThemeTextDark
import aeb.proyecto.habittracker.ui.theme.ThemeTextLight
import aeb.proyecto.habittracker.ui.theme.backgroundDark
import aeb.proyecto.habittracker.ui.theme.borderTextFieldDark
import aeb.proyecto.habittracker.ui.theme.borderTextFieldLight
import aeb.proyecto.habittracker.ui.theme.colorBackgroundCardDark
import aeb.proyecto.habittracker.ui.theme.colorBackgroundCardLight
import aeb.proyecto.habittracker.ui.theme.colorButtonsDark
import aeb.proyecto.habittracker.ui.theme.colorButtonsLight
import aeb.proyecto.habittracker.ui.theme.colorErrorGeneral
import aeb.proyecto.habittracker.ui.theme.colorStatusBarDark
import aeb.proyecto.habittracker.ui.theme.colorStatusBarLight
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColorDark
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColorLight
import aeb.proyecto.habittracker.ui.theme.disableColorDark
import aeb.proyecto.habittracker.ui.theme.disableColorLight
import aeb.proyecto.habittracker.ui.theme.iconColorDark
import aeb.proyecto.habittracker.ui.theme.iconColorLight
import aeb.proyecto.habittracker.ui.theme.terciaryColorAppDark
import aeb.proyecto.habittracker.ui.theme.terciaryColorAppLight
import aeb.proyecto.habittracker.ui.theme.textColorsDark
import aeb.proyecto.habittracker.ui.theme.textColorsLight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ColorsTheme {
    var primaryColorApp by mutableStateOf(backgroundDark)
    var secondaryColorApp by mutableStateOf(backgroundDark)
    var terciaryColorApp by mutableStateOf(terciaryColorAppDark)

    var colorButtons by mutableStateOf(colorButtonsDark)
    var borderTextField by mutableStateOf(borderTextFieldDark)
    var containerTextFieldColor by mutableStateOf(containerTextFieldColorDark)

    var themeText by mutableStateOf(ThemeTextDark)
    var textColors by mutableStateOf(textColorsDark)
    var colorError by mutableStateOf(colorErrorGeneral)

    var colorBackgroundCard by mutableStateOf(colorBackgroundCardDark)
    var colorStatusBar  by mutableStateOf(colorStatusBarDark)

    var colorDisabled by mutableStateOf(disableColorDark)
    var colorIcon by mutableStateOf(iconColorDark)
}

fun setDarkTheme(){
    ColorsTheme.primaryColorApp = backgroundDark
    ColorsTheme.secondaryColorApp = backgroundDark
    ColorsTheme.terciaryColorApp = terciaryColorAppDark
    ColorsTheme.colorButtons = colorButtonsDark
    ColorsTheme.borderTextField = borderTextFieldDark
    ColorsTheme.containerTextFieldColor = containerTextFieldColorDark
    ColorsTheme.themeText = ThemeTextDark
    ColorsTheme.textColors = textColorsDark
    ColorsTheme.colorError = colorErrorGeneral
    ColorsTheme.colorBackgroundCard = colorBackgroundCardDark
    ColorsTheme.colorStatusBar = colorStatusBarDark
    ColorsTheme.colorIcon = iconColorDark
    ColorsTheme.colorDisabled = disableColorDark
}

fun setLightTheme(){
    ColorsTheme.primaryColorApp = backgroundDark
    ColorsTheme.secondaryColorApp = backgroundDark
    ColorsTheme.terciaryColorApp = terciaryColorAppLight
    ColorsTheme.colorButtons = colorButtonsLight
    ColorsTheme.borderTextField = borderTextFieldLight
    ColorsTheme.containerTextFieldColor = containerTextFieldColorLight
    ColorsTheme.themeText = ThemeTextLight
    ColorsTheme.textColors = textColorsLight
    ColorsTheme.colorError = colorErrorGeneral
    ColorsTheme.colorBackgroundCard = colorBackgroundCardLight
    ColorsTheme.colorStatusBar = colorStatusBarLight
    ColorsTheme.colorIcon = iconColorLight
    ColorsTheme.colorDisabled = disableColorLight
}

fun setMode(mode: Int){
    when(mode){
        Constans.DARKMODE -> setDarkTheme()
        Constans.LIGHTMODE -> setLightTheme()
    }
}