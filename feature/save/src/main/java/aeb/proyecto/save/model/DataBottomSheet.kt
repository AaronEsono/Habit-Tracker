package aeb.proyecto.save.model

import aeb.proyecto.save.R
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the various configurations and labels for synchronization-related bottom sheets.
 * Each enum constant maps directly to specific UI resources.
 * * @property title Resource ID for the header title.
 * @property iconTitle The visual representation (icon) for the sheet header.
 * @property label Resource ID for the primary action description or status label.
 */
enum class DataBottomSheet (
    @StringRes var title:Int,
    var iconTitle:ImageVector,
    @StringRes var label:Int
){

    SAVE_HABIT(
        R.string.data_bottomSheet_save_title,
        Icons.Filled.Save,
        R.string.data_bottomSheet_save_label
    ),
    RESTORE_HABIT(
        R.string.data_bottomSheet_restore_title,
        Icons.Filled.SaveAs,
        R.string.data_bottomSheet_restore_label
    ),
    DELETE_HABIT(
        R.string.data_bottomSheet_delete_title,
        Icons.Filled.Delete,
        R.string.data_bottomSheet_delete_label
    ),
    LOG_OUT(
        R.string.data_bottomSheet_logOut_title,
        Icons.AutoMirrored.Filled.Logout,
        R.string.data_bottomSheet_logOut_label
    ),
    ERROR(
        R.string.data_bottomSheet_error_title,
        Icons.Filled.Error,
        R.string.data_bottomSheet_error_label
    ),
    SAVED_DATA(
        R.string.data_bottomSheet_savedData_title,
        Icons.Filled.CloudDone,
        R.string.data_bottomSheet_savedData_label
    ),
    RESTORED_DATA(
        R.string.data_bottomSheet_restoredData_title,
        Icons.Filled.CloudDone,
        R.string.data_bottomSheet_restoredData_label
    ),
    DELETED_DATA(
        R.string.data_bottomSheet_deleteData_title,
        Icons.Filled.Delete,
        R.string.data_bottomSheet_deleteData_label
    )
}