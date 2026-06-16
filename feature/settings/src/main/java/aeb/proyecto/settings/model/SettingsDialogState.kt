package aeb.proyecto.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Manages the current state of the settings UI dialogs.
 * @property showDialog Determines if the modal should be visible.
 * @property dataDialog Holds the configuration for the current dialog type.
 */
data class SettingsDialogState(
    val showDialog:Boolean = false,
    val dataDialog: DataDialog = DataDialog.THEME
)