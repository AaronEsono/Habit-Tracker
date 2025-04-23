package aeb.proyecto.room.utils

import aeb.proyecto.room.model.classes.UnitHabit
import java.math.BigDecimal

fun convertToSeconds(
    principal: String,
    secondary: String,
    unit: UnitHabit
): BigDecimal {
    val main = principal.toIntOrNull() ?: 0
    val secondaryPr = secondary.toIntOrNull() ?: 0

    return when (unit) {
        UnitHabit.HOURS -> {
            // principal = horas, secundaria = minutos
            (main * 3600 + secondaryPr * 60).toBigDecimal()
        }
        UnitHabit.MINUTES -> {
            // principal = minutos, secundaria = segundos
            (main * 60 + secondaryPr).toBigDecimal()
        }
        else -> {
            // Si es en segundos u otro tipo, solo usa principal
            main.toBigDecimal()
        }
    }
}

fun convertFromSeconds(
    value: BigDecimal,
    unit: UnitHabit
): Pair<Int, Int> {
    val totalSeconds = value.toInt()

    return when (unit) {
        UnitHabit.HOURS -> {
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            Pair(hours, minutes)
        }
        UnitHabit.MINUTES -> {
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            Pair(minutes, seconds)
        }
        else -> {
            // Si no aplica, devuelvo el total en el primer valor y 0 en el segundo
            Pair(totalSeconds, 0)
        }
    }
}