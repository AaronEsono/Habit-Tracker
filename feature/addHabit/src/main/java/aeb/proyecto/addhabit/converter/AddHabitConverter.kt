package aeb.proyecto.addhabit.converter

import aeb.proyecto.addhabit.model.AddHabit
import aeb.proyecto.room.entities.habit.Habit
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.model.classes.TypeHabit
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.time.LocalDate

fun fromHabitScreen(habitScreen: AddHabit): HabitWithNotification {
    return HabitWithNotification(
        Habit(
            name = habitScreen.nameTextField.text.toString(),
            description = habitScreen.descriptionTextField.text.toString(),
            color = habitScreen.color.toArgb(),
            icon = habitScreen.icon,
            goal = habitScreen.numberTimesTextField.text.toString().toInt(),
            unit = habitScreen.unit,
            typeHabit = when(habitScreen.typeHabit){
                aeb.proyecto.addhabit.constants.TypeHabit.DAILY -> { TypeHabit.Daily }
                aeb.proyecto.addhabit.constants.TypeHabit.WEEKLY -> { TypeHabit.Weekly(habitScreen.numberOfDaysWeek) }
                aeb.proyecto.addhabit.constants.TypeHabit.MONTHLY -> { TypeHabit.Monthly(habitScreen.numberOfDaysMonth) }
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
        numberOfDaysMonth = (habitWithNotification.habit.typeHabit as? TypeHabit.Monthly)?.numberTimes ?: 1,
        dateRecurringStartDate = (habitWithNotification.habit.typeHabit as? TypeHabit.Recurring)?.date ?: LocalDate.now(),
        intervalTextFieldState = TextFieldState(initialText = (habitWithNotification.habit.typeHabit as? TypeHabit.Recurring)?.interval.toString())
    )
}