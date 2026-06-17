package aeb.proyecto.statistics.utils

import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.utils.convertFromSeconds
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Standard date formatter for short-style local date representations.
 */
val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

/**
 * Formats the habit goal based on its unit type for display purposes.
 * Handles conversion for time-based units (Hours/Minutes) and plain number formatting.
 *
 * @param goal The numerical goal value to be formatted.
 * @param unit The [UnitHabit] type defining how to parse the goal.
 * @return A string representation of the formatted goal.
 */
fun getTextTotal(goal: BigDecimal?, unit: UnitHabit): String {
    return when (unit) {
        UnitHabit.HOURS -> {
            val date = convertFromSeconds(goal?: BigDecimal.ZERO,unit)
            // Formats as HH:mm or mm:ss
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