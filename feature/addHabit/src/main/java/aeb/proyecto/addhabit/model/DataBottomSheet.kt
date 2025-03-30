package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.R
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomSheetState(
    var isVisible: Boolean = false,
    var dataBottomSheet: DataBottomSheet = DataBottomSheet.DELETE_NOTIFICATION,
)


enum class DataBottomSheet(
    val icon:ImageVector,
    @StringRes val title:Int,
    @StringRes val subtitle:Int
){

    DELETE_NOTIFICATION(
        icon = Icons.Default.NotificationsOff,
        title = R.string.add_habit_delete_notification_title,
        subtitle = R.string.add_habit_delete_notification_subtitle
    )

}