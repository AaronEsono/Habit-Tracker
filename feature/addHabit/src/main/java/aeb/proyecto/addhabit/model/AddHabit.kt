package aeb.proyecto.addhabit.model

import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.ui.constants.listColors
import aeb.proyecto.ui.constants.listIcons
import aeb.proyecto.room.model.classes.UnitHabit
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

/**
 * Presentation layer model carrying the mutable and reactive state snapshots of a habit
 * configuration screen form.
 *
 * This aggregate structural snapshot encapsulates text buffer configurations, temporal intervals,
 * identity design assets, and scheduling notification rules necessary to compile or modify a single habit entity.
 *
 * @property id The structural relational primary key tracking footprint, null if initializing a fresh creation flow.
 * @property nameTextField The isolated reactive input text state manager representing the title text buffer of the habit.
 * @property descriptionTextField The isolated reactive input text state manager representing the context description text buffer.
 * @property numberTimesTextField Target goal threshold numeric buffer frequency indicator, defaults to a single event execution instance ("1").
 * @property firstHourTimesTextField Hour-split operational temporal tracker representing primary localized milestone parameters.
 * @property secondHourTimesTextField Hour-split operational temporal tracker representing secondary auxiliary milestone parameters.
 * @property unit The explicit metrics classification [UnitHabit] measuring tracking increments (e.g., Times, Liters, Minutes).
 * @property color The specific [Color] token representation allocated to personalize the habit layout environment.
 * @property icon The targeted [ImageVector] visual asset representing the habit iconography anchor.
 * @property typeHabit The categorical tracking frequency format [TypeHabit] designation (e.g., Daily, Cyclic).
 * @property notifications Immutable structural array listing localized system alarm reminder configurations.
 * @property numberOfDaysWeek Absolute quantitative occurrence goal constraints targeted over an isolated 7-day calendar window.
 * @property weeklyGoal Visual toggle visibility flag defining if an explicit weekly performance threshold is enforced.
 * @property numberOfDaysMonth Absolute quantitative occurrence goal constraints targeted over an isolated monthly calendar window.
 * @property monthlyGoal Visual toggle visibility flag defining if an explicit monthly performance threshold is enforced.
 * @property dateRecurringStartDate The foundational baseline [LocalDate] marking the starting anchor of recurring schedules.
 * @property intervalTextFieldState Text input state tracking numeric day-gaps required exclusively for cyclic recurrence configurations.
 */
data class AddHabit(
    var id:Long? = null,
    var nameTextField:TextFieldState = TextFieldState(),
    var descriptionTextField:TextFieldState = TextFieldState(),

    var numberTimesTextField:TextFieldState = TextFieldState(initialText = "1"),

    var firstHourTimesTextField:TextFieldState = TextFieldState(initialText = "1"),
    var secondHourTimesTextField:TextFieldState = TextFieldState(),

    var unit: UnitHabit = UnitHabit.TIMES,

    val color: Color = listColors[0],
    var icon: ImageVector = listIcons[0],
    var typeHabit: TypeHabit = TypeHabit.DAILY,
    val notifications:List<AddHabitNotification> = listOf(),

    var numberOfDaysWeek:Int = 1,
    var weeklyGoal:Boolean = false,

    var numberOfDaysMonth:Int = 1,
    var monthlyGoal:Boolean = false,

    var dateRecurringStartDate: LocalDate = LocalDate.now(),
    val intervalTextFieldState:TextFieldState = TextFieldState(initialText = "1")
)