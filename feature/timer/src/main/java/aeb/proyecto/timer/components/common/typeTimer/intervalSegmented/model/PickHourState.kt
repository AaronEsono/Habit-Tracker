package aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.model

import aeb.proyecto.timer.R
import aeb.proyecto.timer.model.HourSelectedState

/**
 * Represents the current UI state for the time picker dialog/screen.
 * Used to control dialog visibility and the specific timer mode being edited.
 */
data class PickHourState(
    var showDialog: Boolean = false,
    var typeTimer: TypeTimer = TypeTimer.REST_TIME,
    var hourState: HourSelectedState = HourSelectedState.NoData,
)

/**
 * Defines the available types of timers supported by the application.
 * Each type maps to a [TypePickState] and a specific UI label resource.
 */
enum class TypeTimer (val typePickState: TypePickState, val label:Int){
    WORK_TIME(TypePickState.WORK_TIME, R.string.timer_bottom_sheet_work_title),
    REST_TIME(TypePickState.REST_TIME, R.string.timer_bottom_sheet_rest_title),
}