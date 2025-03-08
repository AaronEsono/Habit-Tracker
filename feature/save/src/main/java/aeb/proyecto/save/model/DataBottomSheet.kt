package aeb.proyecto.save.model

import aeb.proyecto.save.R
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.ui.graphics.vector.ImageVector

enum class DataBottomSheet (
    @StringRes val title:Int,
    val iconTitle:ImageVector,
    @StringRes val label:Int
){

    SAVE_HABIT(
        R.string.data_bottomSheet_save_title,
        Icons.Filled.Save,
        R.string.data_bottomSheet_save_label
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
    )
}