package aeb.proyecto.addhabit.converter

import aeb.proyecto.addhabit.model.AddHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.utils.convertFromSeconds
import aeb.proyecto.room.utils.convertToSeconds
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

fun fromHabitScreen(habitScreen: AddHabit): HabitWithNotification {
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
                aeb.proyecto.addhabit.constants.TypeHabit.MONTHLY -> { TypeHabit.Monthly(habitScreen.numberOfDaysMonth, habitScreen.weeklyGoal) }
                aeb.proyecto.addhabit.constants.TypeHabit.CYCLIC -> { TypeHabit.Recurring(habitScreen.dateRecurringStartDate, habitScreen.intervalTextFieldState.text.toString().toInt()) }
            }
        ),
        notifications = habitScreen.notifications.map {
            fromNotificationScreen(it)
        }.toMutableList()
    )
}

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