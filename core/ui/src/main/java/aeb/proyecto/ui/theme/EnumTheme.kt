package aeb.proyecto.ui.theme

import aeb.proyecto.ui.R

enum class EnumTheme(val theme: Int, val title: Int) {
    DARK(0, R.string.ui_theme_dark),
    MIDNIGHT(1, R.string.ui_theme_midnight),
    SLATE_MOSS(2, R.string.ui_theme_slate_moss),
    PLUM(3, R.string.ui_theme_plum),
    SOFT_STONE(4, R.string.ui_theme_soft_stone),
    ICE_MINT(5, R.string.ui_theme_ice_mint),
    DESERT_ROSE(6, R.string.ui_theme_desert_rose),
    CYBER_DENIM(7, R.string.ui_theme_cyber_denim)
}

fun getTitle(value:Int):Int{
    return EnumTheme.entries.find { it.theme == value }?.title ?: EnumTheme.DARK.title
}