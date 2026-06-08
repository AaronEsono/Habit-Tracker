package aeb.proyecto.addhabit.converter

import aeb.proyecto.addhabit.model.AddHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.utils.convertFromSeconds
import aeb.proyecto.room.utils.convertToSeconds
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

/**
 * Transforms a presentation layer aggregate configuration form model into a consolidated
 * domain-layer relational structure payload.
 *
 * Unwraps reactive inputs into standard primitives, casts colors into ARGB integer matrices,
 * computes complex goal time structures into flat storage units, and converts UI presentation
 * enum flags into deep polymorphic domain behaviors.
 *
 * @param habitScreen The active [AddHabit] screen state model containing the current input parameters.
 * @return A unified [HabitWithNotification] domain data token ready for persistence layers.
 */
fun fromHabitScreen(habitScreen: AddHabit): HabitWithNotification {
    Log.d("HabitScreen", habitScreen.toString())
    return HabitWithNotification(
        Habit(
            name = habitScreen.nameTextField.text.toString(),
            description = habitScreen.descriptionTextField.text.toString(),
            color = habitScreen.color.toArgb(),
            icon = habitScreen.icon,
            goal = goalConverter(
                habitScreen.numberTimesTextField,
                habitScreen.firstHourTimesTextField,
                habitScreen.secondHourTimesTextField,
                habitScreen.unit
            ),
            unit = habitScreen.unit,
            typeHabit = when(habitScreen.typeHabit){
                aeb.proyecto.addhabit.constants.TypeHabit.DAILY -> { TypeHabit.Daily }
                aeb.proyecto.addhabit.constants.TypeHabit.WEEKLY -> { TypeHabit.Weekly(habitScreen.numberOfDaysWeek, habitScreen.weeklyGoal) }
                aeb.proyecto.addhabit.constants.TypeHabit.MONTHLY -> { TypeHabit.Monthly(habitScreen.numberOfDaysMonth, habitScreen.monthlyGoal) }
                aeb.proyecto.addhabit.constants.TypeHabit.CYCLIC -> { TypeHabit.Recurring(habitScreen.dateRecurringStartDate, habitScreen.intervalTextFieldState.text.toString().toInt()) }
            }
        ),
        notifications = habitScreen.notifications.map {
            fromNotificationScreen(it)
        }.toMutableList()
    )
}

/**
 * Rehydrates a domain-layer composite data entity back into an isolated, mutable presentation form state structure.
 *
 * Maps textual properties into fresh Foundation [TextFieldState] controllers, reconstructs graphic
 * color tokens from primitive storage integers, and safely unpacks polymorphic rule definitions into
 * separate layout fields.
 *
 * @param habitWithNotification The underlying [HabitWithNotification] aggregate structural payload retrieved from core storage.
 * @return An interactive, independent [AddHabit] configuration instance model prepared for UI state tracking.
 */
fun toHabitScreen(habitWithNotification: HabitWithNotification): AddHabit {
    return AddHabit(
        id = habitWithNotification.habit.id,
        nameTextField = TextFieldState(initialText = habitWithNotification.habit.name),
        descriptionTextField = TextFieldState(initialText = habitWithNotification.habit.description ?: ""),
        numberTimesTextField = TextFieldState(initialText = habitWithNotification.habit.goal.toString()),
        unit = habitWithNotification.habit.unit,
        color = Color(habitWithNotification.habit.color),
        icon = habitWithNotification.habit.icon,
        typeHabit = when(habitWithNotification.habit.typeHabit){
            is TypeHabit.Daily -> { aeb.proyecto.addhabit.constants.TypeHabit.DAILY }
            is TypeHabit.Weekly -> { aeb.proyecto.addhabit.constants.TypeHabit.WEEKLY }
            is TypeHabit.Monthly -> { aeb.proyecto.addhabit.constants.TypeHabit.MONTHLY }
            is TypeHabit.Recurring -> { aeb.proyecto.addhabit.constants.TypeHabit.CYCLIC }
        },
        notifications = habitWithNotification.notifications.map {
            toNotificationScreen(it)
        },
        numberOfDaysWeek = (habitWithNotification.habit.typeHabit as? TypeHabit.Weekly)?.numberDays ?: 1,
        weeklyGoal = (habitWithNotification.habit.typeHabit as? TypeHabit.Weekly)?.weeklyGoal ?: false,
        numberOfDaysMonth = (habitWithNotification.habit.typeHabit as? TypeHabit.Monthly)?.numberTimes ?: 1,
        monthlyGoal = (habitWithNotification.habit.typeHabit as? TypeHabit.Monthly)?.monthlyGoal ?: false,
        dateRecurringStartDate = (habitWithNotification.habit.typeHabit as? TypeHabit.Recurring)?.date ?: LocalDate.now(),
        intervalTextFieldState = TextFieldState(initialText = (habitWithNotification.habit.typeHabit as? TypeHabit.Recurring)?.interval.toString()),
        firstHourTimesTextField = firstHourConverter(habitWithNotification.habit.goal,habitWithNotification.habit.unit),
        secondHourTimesTextField = secondHourConverter(habitWithNotification.habit.goal,habitWithNotification.habit.unit)
    )
}

/**
 * Compiles input parameters across text fields into a standardized financial-grade numeric quantitative metric representation.
 * Dispatches temporal fields through conversion filters if tracking metric units measure time bounds;
 * otherwise, normalizes base texts into isolated scales to prevent structural decimal truncation leaks.
 *
 * @param numberTextField Standard discrete count input state buffer tracker.
 * @param firstHourTextField Temporal hours field boundary text input buffer tracker.
 * @param secondHourTextField Temporal minutes field boundary text input buffer tracker.
 * @param unitHabit The structural metrics definition format targeting the evaluation.
 * @return A high-precision [BigDecimal] numerical matrix anchor representing the target operational goal.
 */
fun goalConverter(
    numberTextField: TextFieldState,
    firstHourTextField: TextFieldState,
    secondHourTextField: TextFieldState,
    unitHabit: UnitHabit
):BigDecimal{
    return when(unitHabit){
        UnitHabit.MINUTES, UnitHabit.HOURS -> {
            convertToSeconds(firstHourTextField.text.toString(),secondHourTextField.text.toString(),unitHabit)
        }
        else -> {
            numberTextField.text
                .toString()
                .toBigDecimalOrNull()
                ?.setScale(3, RoundingMode.HALF_UP)
                ?.stripTrailingZeros() ?: BigDecimal.ZERO
        }
    }
}

/**
 * Extracts and maps the primary tracking segment from a consolidated quantitative target numerical entity
 * into a presentation layer input controller state.
 *
 * @param goal The composite target metric [BigDecimal] specification ledger retrieved from the core data framework.
 * @param unitHabit The active metric measurement classification rule tracking parameters.
 * @return A fresh [TextFieldState] model wrapped around isolated target string elements.
 */
fun firstHourConverter(goal:BigDecimal,unitHabit: UnitHabit):TextFieldState{
    return when(unitHabit){
        UnitHabit.MINUTES, UnitHabit.HOURS -> {
            val (hours,_) = convertFromSeconds(goal,unitHabit)
            TextFieldState(hours)
        }
        else -> {
            TextFieldState(initialText = "1")
        }
    }
}

/**
 * Extracts and maps the secondary tracking segment from a consolidated quantitative target numerical entity
 * into a presentation layer input controller state.
 *
 * @param goal The composite target metric [BigDecimal] specification ledger retrieved from the core data framework.
 * @param unitHabit The active metric measurement classification rule tracking parameters.
 * @return A fresh [TextFieldState] model wrapped around isolated target string elements.
 */
fun secondHourConverter(goal:BigDecimal,unitHabit: UnitHabit):TextFieldState{
    return when(unitHabit){
        UnitHabit.MINUTES, UnitHabit.HOURS -> {
            val (_,second) = convertFromSeconds(goal,unitHabit)
            TextFieldState(second)
        }
        else -> {
            TextFieldState(initialText = "1")
        }
    }
}