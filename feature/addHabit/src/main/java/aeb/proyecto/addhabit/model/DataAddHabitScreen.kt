package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.constants.listColors
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

/**
 * Consolidated master screen layout configuration model mapping the active data aggregates,
 * transactional statuses, and interactive overlay visibilities of the AddHabit screen.
 *
 * @property habitScreen The underlying baseline form entity [AddHabit] tracking current configuration metrics.
 * @property contrastColor An optimized, accessible [Color] token computed to guarantee visual balance over dynamic custom themes.
 * @property dayStartWeek The localized calendar baseline [DayOfWeek] parameter retrieved from individual preferences.
 * @property isColorSelected Visibility tracking matrix state monitoring if the interactive color picker grid overlay is mounted.
 * @property isIconSelected Visibility tracking matrix state monitoring if the interactive icon picker grid overlay is mounted.
 * @property showDialog Global flag controlling whether a modal dialogue component should be rendered on the active viewport window.
 * @property typeDialog Operational integer route key mapping which specific contextual dialogue layout asset to initialize.
 * @property notificationSelected Temporary operational cache storing the targeted notification profile during edits or deletions.
 * @property bottomSheetState Aggregate presentation model managing visibility and type contexts for the structural operational BottomSheet.
 */
data class DataAddHabitScreen(
    var habitScreen: AddHabit = AddHabit(),

    var contrastColor:Color = getContrastColor(listColors[0]),
    var dayStartWeek: DayOfWeek = DayOfWeek.MONDAY,

    var isColorSelected:Boolean = false,
    var isIconSelected:Boolean = false,

    val showDialog:Boolean = false,
    val typeDialog:Int = PICK_TYPE_HABIT,

    val notificationSelected:AddHabitNotification = AddHabitNotification(),
    val bottomSheetState: BottomSheetState = BottomSheetState()
)