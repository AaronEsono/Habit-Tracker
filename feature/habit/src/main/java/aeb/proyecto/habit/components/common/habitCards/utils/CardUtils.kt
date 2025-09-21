package aeb.proyecto.habit.components.common.habitCards.utils

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.utils.convertFromSeconds
import java.math.BigDecimal
import java.time.LocalDate

fun getUnitTitle(unitHabit: UnitHabit, timesDone: BigDecimal): Int {
    return when(unitHabit){
        UnitHabit.MINUTES -> {
            if (timesDone <= BigDecimal(60)) unitHabit.title else unitHabit.titlePlural
        }
        UnitHabit.HOURS -> {
            if (timesDone <= BigDecimal(3600)) unitHabit.title else unitHabit.titlePlural
        }
        else -> {
            if (timesDone == BigDecimal(1)) unitHabit.title else unitHabit.titlePlural
        }
    }
}

fun getSelected(dateSelected: LocalDate, dailyHabits:List<HabitDay>): HabitDay?{
    return dailyHabits.find {date -> date.date == dateSelected}
}

fun getTextTotal(goal: BigDecimal?, unit: UnitHabit): String {
    return when (unit) {
        UnitHabit.HOURS -> {
            val date = convertFromSeconds(goal?: BigDecimal.ZERO,unit)
            "${date.first}:${date.second}"
        }

        UnitHabit.MINUTES -> {
            val date = convertFromSeconds(goal?: BigDecimal.ZERO,unit)
            "${date.first}:${date.second}"
        }

        else -> {
            goal?.toPlainString() ?: "0"
        }
    }
}