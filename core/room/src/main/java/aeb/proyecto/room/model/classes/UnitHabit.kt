package aeb.proyecto.room.model.classes

import aeb.proyecto.room.R
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TravelExplore

enum class UnitHabit(
    val icon:ImageVector,
    @StringRes val title:Int,
    @StringRes val titlePlural:Int,
    val unitType: TIPO_UNIDAD,
){
    // TIEMPO
    SECONDS(
        icon = Icons.Filled.AccessTime,
        title = R.string.add_habit_second_singular,
        titlePlural = R.string.add_habit_seconds,
        unitType = TIPO_UNIDAD.TIEMPO
    ),
    MINUTES(
        icon = Icons.Filled.Timer,
        title = R.string.add_habit_minute_singular,
        titlePlural = R.string.add_habit_minutes,
        unitType = TIPO_UNIDAD.TIEMPO
    ),
    HOURS(
        icon = Icons.Filled.HourglassEmpty,
        title = R.string.add_habit_hour_singular,
        titlePlural = R.string.add_habit_hours,
        unitType = TIPO_UNIDAD.TIEMPO,
    ),

    // CANTIDAD
    STEPS(
        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
        title = R.string.add_habit_step_singular,
        titlePlural = R.string.add_habit_steps,
        unitType = TIPO_UNIDAD.CANTIDAD
    ),
    PAGES(
        icon = Icons.Filled.Book,
        title = R.string.add_habit_page_singular,
        titlePlural = R.string.add_habit_pages,
        unitType = TIPO_UNIDAD.CANTIDAD
    ),
    CALORIES(
        icon = Icons.Filled.LocalFireDepartment,
        title = R.string.add_habit_calorie_singular,
        titlePlural = R.string.add_habit_calories,
        unitType = TIPO_UNIDAD.CANTIDAD
    ),
    KILOMETERS(
        icon = Icons.Filled.TravelExplore,
        title = R.string.add_habit_kilometer_singular,
        titlePlural = R.string.add_habit_kilometers,
        unitType = TIPO_UNIDAD.CANTIDAD,
    ),
    EXERCISES(
        icon = Icons.Filled.FitnessCenter,
        title = R.string.add_habit_exercise_singular,
        titlePlural = R.string.add_habit_exercises,
        unitType = TIPO_UNIDAD.CANTIDAD
    ),
    REPETITIONS(
        icon = Icons.Filled.Repeat,
        title = R.string.add_habit_repetition_singular,
        titlePlural = R.string.add_habit_repetitions,
        unitType = TIPO_UNIDAD.CANTIDAD
    ),
    // FRECUENCIA
    TIMES(
        icon = Icons.Filled.Numbers,
        title = R.string.add_habit_time_singular,
        titlePlural = R.string.add_habit_times,
        unitType = TIPO_UNIDAD.FRECUENCIA
    ),
    SESSIONS(
        icon = Icons.Filled.CheckCircle,
        title = R.string.add_habit_session_singular,
        titlePlural = R.string.add_habit_sessions,
        unitType = TIPO_UNIDAD.FRECUENCIA
    ),
    TASKS(
        icon = Icons.AutoMirrored.Filled.Assignment,
        title = R.string.add_habit_task_singular,
        titlePlural = R.string.add_habit_tasks,
        unitType = TIPO_UNIDAD.FRECUENCIA
    ),
    ATTEMPTS(
        icon = Icons.Filled.EventRepeat,
        title = R.string.add_habit_attempt_singular,
        titlePlural = R.string.add_habit_attempts,
        unitType = TIPO_UNIDAD.FRECUENCIA
    )
}


enum class TIPO_UNIDAD{
    FRECUENCIA,
    CANTIDAD,
    TIEMPO,
}

val listTime = listOf(
    UnitHabit.SECONDS,
    UnitHabit.MINUTES,
    UnitHabit.HOURS
)

val listQuantity = listOf(
    UnitHabit.STEPS,
    UnitHabit.PAGES,
    UnitHabit.CALORIES,
    UnitHabit.KILOMETERS,
    UnitHabit.EXERCISES,
    UnitHabit.REPETITIONS
)

val listFrequency = listOf(
    UnitHabit.TIMES,
    UnitHabit.SESSIONS,
    UnitHabit.TASKS,
    UnitHabit.ATTEMPTS
)

val unitsHourMode = listOf(
    UnitHabit.MINUTES,
    UnitHabit.HOURS
)