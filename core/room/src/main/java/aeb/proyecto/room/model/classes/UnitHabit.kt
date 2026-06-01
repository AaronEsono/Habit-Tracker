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

/**
 * Type-safe metric configuration tracking registry bounding quantitative completion metrics.
 *
 * Each structural constant acts as a metadata container holding references to graphic assets,
 * localized string resources targeting singular/plural variations, and its structural categorization.
 *
 * @property icon The foundational vector graphic layout asset associated with this metric dimension.
 * @property title The localization [StringRes] target pointer representing the singular format (e.g., "Minute").
 * @property titlePlural The localization [StringRes] target pointer representing the plural format (e.g., "Minutes").
 * @property unitType The core [UnitType] framework layer mapping this metric to a functional domain group.
 */
enum class UnitHabit(
    val icon:ImageVector,
    @StringRes val title:Int,
    @StringRes val titlePlural:Int,
    val unitType: UnitType,
){
    // TIEMPO
    SECONDS(
        icon = Icons.Filled.AccessTime,
        title = R.string.add_habit_second_singular,
        titlePlural = R.string.add_habit_seconds,
        unitType = UnitType.TIME
    ),
    MINUTES(
        icon = Icons.Filled.Timer,
        title = R.string.add_habit_minute_singular,
        titlePlural = R.string.add_habit_minutes,
        unitType = UnitType.TIME
    ),
    HOURS(
        icon = Icons.Filled.HourglassEmpty,
        title = R.string.add_habit_hour_singular,
        titlePlural = R.string.add_habit_hours,
        unitType = UnitType.TIME,
    ),

    // CANTIDAD
    STEPS(
        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
        title = R.string.add_habit_step_singular,
        titlePlural = R.string.add_habit_steps,
        unitType = UnitType.QUANTITY
    ),
    PAGES(
        icon = Icons.Filled.Book,
        title = R.string.add_habit_page_singular,
        titlePlural = R.string.add_habit_pages,
        unitType = UnitType.QUANTITY
    ),
    CALORIES(
        icon = Icons.Filled.LocalFireDepartment,
        title = R.string.add_habit_calorie_singular,
        titlePlural = R.string.add_habit_calories,
        unitType = UnitType.QUANTITY
    ),
    KILOMETERS(
        icon = Icons.Filled.TravelExplore,
        title = R.string.add_habit_kilometer_singular,
        titlePlural = R.string.add_habit_kilometers,
        unitType = UnitType.QUANTITY,
    ),
    EXERCISES(
        icon = Icons.Filled.FitnessCenter,
        title = R.string.add_habit_exercise_singular,
        titlePlural = R.string.add_habit_exercises,
        unitType = UnitType.QUANTITY
    ),
    REPETITIONS(
        icon = Icons.Filled.Repeat,
        title = R.string.add_habit_repetition_singular,
        titlePlural = R.string.add_habit_repetitions,
        unitType = UnitType.QUANTITY
    ),
    // FRECUENCIA
    TIMES(
        icon = Icons.Filled.Numbers,
        title = R.string.add_habit_time_singular,
        titlePlural = R.string.add_habit_times,
        unitType = UnitType.FREQUENCY
    ),
    SESSIONS(
        icon = Icons.Filled.CheckCircle,
        title = R.string.add_habit_session_singular,
        titlePlural = R.string.add_habit_sessions,
        unitType = UnitType.FREQUENCY
    ),
    TASKS(
        icon = Icons.AutoMirrored.Filled.Assignment,
        title = R.string.add_habit_task_singular,
        titlePlural = R.string.add_habit_tasks,
        unitType = UnitType.FREQUENCY
    ),
    ATTEMPTS(
        icon = Icons.Filled.EventRepeat,
        title = R.string.add_habit_attempt_singular,
        titlePlural = R.string.add_habit_attempts,
        unitType = UnitType.FREQUENCY
    )
}

/**
 * Contextual classification identifying the foundational measurement nature of a configured habit unit.
 */
enum class UnitType{
    FREQUENCY,
    QUANTITY,
    TIME,
}

/** Static shorthand collection targeting exclusively time-based metric indices. */
val listTime = listOf(
    UnitHabit.SECONDS,
    UnitHabit.MINUTES,
    UnitHabit.HOURS
)

/** Static shorthand collection targeting exclusively quantitative capacity metric indices. */
val listQuantity = listOf(
    UnitHabit.STEPS,
    UnitHabit.PAGES,
    UnitHabit.CALORIES,
    UnitHabit.KILOMETERS,
    UnitHabit.EXERCISES,
    UnitHabit.REPETITIONS
)

/** Static shorthand collection targeting exclusively recurrence or cadence metric indices. */
val listFrequency = listOf(
    UnitHabit.TIMES,
    UnitHabit.SESSIONS,
    UnitHabit.TASKS,
    UnitHabit.ATTEMPTS
)

/** Filtered structural collection limiting execution layouts strictly to macro-temporal intervals. */
val unitsHourMode = listOf(
    UnitHabit.MINUTES,
    UnitHabit.HOURS
)