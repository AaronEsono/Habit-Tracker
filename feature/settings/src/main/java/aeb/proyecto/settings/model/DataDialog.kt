package aeb.proyecto.settings.model

import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.settings.R
import aeb.proyecto.ui.date.DaysWeek
import aeb.proyecto.ui.month.EnumMonths
import aeb.proyecto.ui.theme.EnumTheme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.Flow

/**
 * Defines the configuration for each settings dialog.
 * Encapsulates UI resources and the specific data model required for each option.
 */
enum class DataDialog(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    val dialogComponent: DialogElements
) {
    THEME(
        R.drawable.im_theme,
        R.string.settings_theme_pick,
        DialogElements.DialogTheme(EnumTheme.entries)
    ),
    LANGUAGE(
        R.drawable.im_language,
        R.string.settings_language_pick,
        DialogElements.DialogLanguage(EnumLanguage.entries)
    ),
    DAY_WEEK(
        R.drawable.im_calendar,
        R.string.settings_day_pick,
        DialogElements.DialogDayWeek(DaysWeek.entries)
    ),
}

/**
 * Sealed class providing type-safe access to the data required by the dialogs.
 */
sealed class DialogElements {
    data class DialogTheme(val theme:List<EnumTheme>):DialogElements()
    data class DialogLanguage(val language: List<EnumLanguage>):DialogElements()
    data class DialogDayWeek(val dayWeek: List<DaysWeek>):DialogElements()
}