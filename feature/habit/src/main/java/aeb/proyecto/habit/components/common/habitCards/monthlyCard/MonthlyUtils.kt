package aeb.proyecto.habit.components.common.habitCards.monthlyCard

import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


fun getDates(
    startOfMonth: LocalDate,
    startDayWeek:DayOfWeek?,
    habits: HabitWithDailyHabit
): CalendarUIState<HabitWithDay>{

    val yearMonth = YearMonth.of(startOfMonth.year, startOfMonth.month)
    val firstDay = startDayWeek ?: DayOfWeek.MONDAY
    val calendarDataSource = CalendarDataSource()

    val getDates = calendarDataSource.getDates(firstDay,yearMonth){ date ->
        HabitWithDay(
            habit = habits.habit,
            day = getSelected(date,habits.dailyHabits) ?: HabitDay()
        )
    }

    return CalendarUIState(getDates)
}

fun daysCompletedOnAMonth(habits: HabitWithDailyHabit, startOfMonth: LocalDate):BigDecimal{
    val daysInThisMonth = startOfMonth.lengthOfMonth()

    return habits.dailyHabits.filter {
        it.date in startOfMonth..startOfMonth.plusDays(daysInThisMonth.toLong())
                && it.goalDone >= habits.habit.goal
    }.size.toBigDecimal()
}

fun numberOfDaysToComplete(goalToDo: Int, startOfMonth: LocalDate):Int{
    val daysInThisMonth = startOfMonth.lengthOfMonth()
    return if(daysInThisMonth < goalToDo) daysInThisMonth else goalToDo
}