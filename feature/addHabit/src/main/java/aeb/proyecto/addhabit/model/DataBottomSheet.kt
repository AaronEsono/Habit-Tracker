package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.R
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Presentation snapshot monitoring the active operational and visibility layouts
 * of the contextual overlay BottomSheet.
 *
 * @property isVisible Global tracking matrix flag defining if the BottomSheet component is mounted on the viewport.
 * @property dataBottomSheet The explicit categorical context payload model mapping operational details onto the sheet layout.
 */
data class BottomSheetState(
    var isVisible: Boolean = false,
    var dataBottomSheet: DataBottomSheet = DataBottomSheet.DELETE_NOTIFICATION,
)

/**
 * Explicit configuration enum descriptor mapping localization resources, semantics,
 * and iconography vectors across diverse operational contexts of the presentation BottomSheet.
 *
 * @property icon The targeted [ImageVector] asset chosen to represent the core intent or alert state visually.
 * @property title The explicit Android platform [StringRes] resource pointer mapping the main header string literal.
 * @property subtitle The explicit Android platform [StringRes] resource pointer mapping the contextual description string literal.
 */
enum class DataBottomSheet(
    val icon:ImageVector,
    @StringRes val title:Int,
    @StringRes val subtitle:Int
){

    /**
     * Context alert tracking destructive reminder removal workflows.
     */
    DELETE_NOTIFICATION(
        icon = Icons.Default.NotificationsOff,
        title = R.string.add_habit_delete_notification_title,
        subtitle = R.string.add_habit_delete_notification_subtitle
    ),

    /**
     * Operational constraint violation triggered when required title identification fields or naming scopes are missing.
     */
    ERROR_NAME_UNIT(
        icon = Icons.Default.Error,
        title = R.string.add_habit_error_general_title,
        subtitle = R.string.add_habit_error_no_name_subtite
    ),

    /**
     * Operational constraint violation triggered when invalid recurrence intervals are configured inside cyclic routine structures.
     */
    ERROR_INTERVAL_UNIT(
        icon = Icons.Default.Error,
        title = R.string.add_habit_error_general_title,
        subtitle = R.string.add_habit_error_interval_days_subtite
    ),

    /**
     * Operational constraint violation triggered when chronological duration allocations yield invalid parameters.
     */
    ERROR_HOUR(
        icon = Icons.Default.Error,
        title = R.string.add_habit_error_general_title,
        subtitle = R.string.add_habit_error_unit_hout
    ),

    /**
     * Generic systemic transactional crash fallback sheet configuration designed to catch unexpected hardware or persistence failures.
     */
    GENERAL_ERROR(
        icon = Icons.Default.Error,
        title = R.string.add_habit_error_general_title,
        subtitle = R.string.add_habit_error_general_subtite
    )
}