package aeb.proyecto.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SettingsDialogState(
    val showDialog:Boolean = false,
    val dataDialog: TypeDialog = TypeDialog.PickThemeLanguage(DataDialog.THEME)
)