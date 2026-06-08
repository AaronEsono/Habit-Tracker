package aeb.proyecto.addhabit.constants

import aeb.proyecto.addhabit.R
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.DayOfWeek

// ============================================================================
// CONTEXTUAL MODAL ROUTING CONSTANTS
// ============================================================================

/**
 * Identifier token representing the modal dialog layer for choosing the core tracking frequency (Daily, Weekly, etc.).
 */
const val PICK_TYPE_HABIT = 1

/**
 * Identifier token representing the modal dialog layer for configuring the calendar boundary start date.
 */
const val PICK_DATE = 2

/**
 * Identifier token representing the modal dialog layer for changing the quantitative performance measurement metric.
 */
const val PICK_UNIT = 3

/**
 * Identifier token representing the modal dialog layer for configuring basic notification style classifications.
 */
const val PICK_TYPE_NOTIFICATION = 4

/**
 * Identifier token representing the interactive system overlay time-picker component dialogue screen.
 */
const val PICK_NOTIFICATION = 5

// ============================================================================
// ENUM CONFIGURATION STRUCTURES
// ============================================================================

/**
 * Enumeration ledger mapping UI string resource pointers and baseline domain configuration models
 * for system reminder alert style patterns.
 *
 * @property title The explicit Android platform [StringRes] resource pointer mapping the title string header.
 * @property label The explicit Android platform [StringRes] resource pointer mapping descriptive helper labels.
 * @property type The structural domain classification [TypeNotification] rule attached to this setup.
 */
enum class TypeNotifications(
    @StringRes val title: Int,
    @StringRes val label: Int,
    val type: TypeNotification
) {

    /**
     * Notification triggers attached to precise fixed weekday grids.
     */
    DAILY(
        R.string.add_habit_daily_notification_title,
        R.string.add_habit_daily_notification_label,
        TypeNotification.Daily()
    ),

    /**
     * Notification triggers attached to a sliding day-gap frequency sequence.
     */
    CYCLIC(
        R.string.add_habit_cyclic_notification_title,
        R.string.add_habit_cyclic_notification_label,
        TypeNotification.Recurring()
    )
}

/**
 * Core tracking behavioral rules architecture specifying the lifecycle type parameters of the target habit.
 *
 * @property id Unique operational identity code mapping the behavior pattern.
 * @property title The explicit Android platform [StringRes] resource pointer tracking the core frequency title literal.
 * @property subtitle The explicit Android platform [StringRes] resource pointer tracking the descriptive helper context literal.
 */
enum class TypeHabit(val id: Int, @StringRes val title: Int, @StringRes val subtitle: Int) {
    DAILY(1, R.string.add_habit_type_habit_daily, R.string.add_habit_type_habit_daily_label),
    WEEKLY(2, R.string.add_habit_type_habit_weekly, R.string.add_habit_type_habit_weekly_label),
    MONTHLY(3, R.string.add_habit_type_habit_monthly, R.string.add_habit_type_habit_monthly_label),
    CYCLIC(4, R.string.add_habit_type_habit_cyclic, R.string.add_habit_type_habit_cyclic_label)
}

/**
 * Structural marker separating the display and content mapping scopes inside the grid asset selector overlay sheet.
 */
enum class GridOption(){
    COLORS,
    ICONS
}

// ============================================================================
// POLYMORPHIC INTERACTION RESULT WRAPPERS
// ============================================================================

/**
 * Sealed variant mapping transaction results emitted from selecting personalized aesthetic assets in localized grid selectors.
 */
sealed class GridOptionResult(){
    /**
     * Carries the precise design [Color] token value picked by the user.
     */
    data class colorResult(val color: Color): GridOptionResult()

    /**
     * Carries the target graphical vector asset [ImageVector] picked by the user.
     */
    data class iconResult(val icon: ImageVector): GridOptionResult()
}

/**
 * Sealed transaction result layout carrying precise state mutation payloads emitted from dynamic reminder item panels.
 */
sealed class TypeNotificationResult{

    /**
     * Payload capturing a weekday selection modification event inside a fixed calendar grid tracking matrix.
     *
     * @property day The specific [DayOfWeek] interaction anchor whose selection matrix toggled.
     * @property id The cryptographic notification code identifier routing the target event.
     */
    data class Daily(val day:DayOfWeek, val id:String):TypeNotificationResult()

    /**
     * Payload capturing day-gap step adjustments within a rolling cyclic alarm sequence.
     *
     * @property action Boolean instruction toggle parameter where true commands an increment (+1) and false a decrement (-1).
     * @property id The cryptographic notification code identifier routing the target event.
     */
    data class Recurring(val action:Boolean, val id:String):TypeNotificationResult()
}