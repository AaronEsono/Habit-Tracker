package aeb.proyecto.habit.components.common.habitCards.monthlyCard

import aeb.proyecto.habit.components.common.habitCards.utils.getSelected
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


fun getDates(
    startOfMonth: LocalDate,
    startDayWeek:DayOfWeek?,
    habits: HabitWithDailyHabit
): CalendarUIState<HabitDay>{

    val yearMonth = YearMonth.of(startOfMonth.year, startOfMonth.month)
    val firstDay = startDayWeek ?: DayOfWeek.MONDAY
    val calendarDataSource = CalendarDataSource()

    val getDates = calendarDataSource.getDates(firstDay,yearMonth){ date ->
        getSelected(date,habits.dailyHabits)
    }

    return CalendarUIState(getDates)
}