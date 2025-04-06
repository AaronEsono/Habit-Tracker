package aeb.proyecto.settings.model

import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.settings.R
import aeb.proyecto.ui.theme.EnumTheme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.Flow

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
    )
}

sealed class DialogElements {
    data class DialogTheme(val theme:List<EnumTheme>):DialogElements()
    data class DialogLanguage(val language: List<EnumLanguage>):DialogElements()
}

sealed class TypeDialog{
    data class PickThemeLanguage(val dataDialog: DataDialog):TypeDialog()
    data object GeneralSettings:TypeDialog()
}