package aeb.proyecto.timer.components.commom.typeTimer.intervalSegmented.model

import aeb.proyecto.timer.R
import aeb.proyecto.timer.model.HourSelectedState

data class PickHourState(
    var showDialog: Boolean = false,
    var typeTimer: TypeTimer = TypeTimer.REST_TIME,
    var hourState: HourSelectedState = HourSelectedState.NoData,
)

enum class TypeTimer (val typePickState: TypePickState, val label:Int){
    WORK_TIME(TypePickState.WORK_TIME, R.string.timer_bottom_sheet_work_title),
    REST_TIME(TypePickState.REST_TIME, R.string.timer_bottom_sheet_rest_title),
}