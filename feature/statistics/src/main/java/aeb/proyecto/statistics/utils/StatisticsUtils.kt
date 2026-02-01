package aeb.proyecto.statistics.utils

import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.utils.convertFromSeconds
import java.math.BigDecimal

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