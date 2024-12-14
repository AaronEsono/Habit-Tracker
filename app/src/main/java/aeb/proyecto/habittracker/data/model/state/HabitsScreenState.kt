package aeb.proyecto.habittracker.data.model.state

import aeb.proyecto.habittracker.R
import java.time.LocalDate

data class HabitsScreenState(
    val showDialog: Boolean = false,
    val showGeneralDx: Boolean = false,
    val showCalendar: Boolean = false,
    val showSteps: Boolean = false,
    val date: LocalDate? = null,
    val textAttention:Int = R.string.general_dx_subtitle_delete
)