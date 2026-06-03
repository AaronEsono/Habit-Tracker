package aeb.proyecto.ui.theme

import aeb.proyecto.ui.R

/**
 * Global design system theme repository cataloging the application's visual layout personalities.
 * Pairs persistent database integer identification flags directly with localized [StringRes] resource
 * pointers, allowing seamless run-time UI color matrix mutations.
 *
 * Houses the application's flagship minimal, monochromatic, and sub-toned color scheme declarations.
 *
 * @property theme The primitive persistent integer marker stored inside local user preferences.
 * @property title The compiler-guaranteed string resource identifier pointer used to render localized titles.
 */
enum class EnumTheme(val theme: Int, val title: Int) {

    /** High-contrast baseline dark layout configuration. */
    DARK(0, R.string.ui_theme_dark),

    /** Ultra-deep pure black canvas layout optimized for OLED displays. */
    MIDNIGHT(1, R.string.ui_theme_midnight),

    /** Balanced organic layout pairing earthy green sub-tones with desaturated slates. */
    SLATE_MOSS(2, R.string.ui_theme_slate_moss),

    /** Deep dark-mode profile highlighted by warm, minimal plum accents. */
    PLUM(3, R.string.ui_theme_plum),

    /** High-end light-mode layout built over calm stone surfaces and soft, clean contrasts. */
    SOFT_STONE(4, R.string.ui_theme_soft_stone),

    /** Refreshing clear layout utilizing icy mint sub-tones for crisp geometric spacing. */
    ICE_MINT(5, R.string.ui_theme_ice_mint),

    /** Muted, sophisticated warm theme embracing dusty desert rose profiles. */
    DESERT_ROSE(6, R.string.ui_theme_desert_rose),

    /** High-density technical dark layout leaning on minimal cyan-tinted cyber denims. */
    CYBER_DENIM(7, R.string.ui_theme_cyber_denim)
}

/**
 * Resolves the localized string resource token corresponding to a persistent theme index value.
 * Implements a strict boundary fallback to the primary dark theme [EnumTheme.DARK] if the incoming marker is out-of-bounds.
 *
 * @param value The primitive persistent identifier index checked against the theme matrix.
 * @return The functional compiler-guaranteed [StringRes] integer pointer.
 */
fun getTitle(value:Int):Int{
    return EnumTheme.entries.find { it.theme == value }?.title ?: EnumTheme.DARK.title
}