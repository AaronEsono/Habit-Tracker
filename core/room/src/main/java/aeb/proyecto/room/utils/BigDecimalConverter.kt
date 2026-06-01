package aeb.proyecto.room.utils

import aeb.proyecto.room.model.classes.UnitHabit
import java.math.BigDecimal

/**
 * Collapses high-level dual-input time pickers (e.g., Hours + Minutes) into a single unified
 * scalar [BigDecimal] representation of total seconds.
 *
 * @param principal The primary input value string sequence (representing Hours or Minutes depending on [unit]).
 * @param secondary The auxiliary input value string sequence (representing Minutes or Seconds depending on [unit]).
 * @param unit The target [UnitHabit] measuring configuration blueprint.
 * @return A precise [BigDecimal] capturing the total aggregated volume expressed strictly in seconds.
 */
fun convertToSeconds(
    principal: String,
    secondary: String,
    unit: UnitHabit
): BigDecimal {
    val main = principal.toIntOrNull() ?: 0
    val secondaryPr = secondary.toIntOrNull() ?: 0

    return when (unit) {
        UnitHabit.HOURS -> {
            // principal = hours, secondary = minutes
            (main * 3600 + secondaryPr * 60).toBigDecimal()
        }
        UnitHabit.MINUTES -> {
            // principal = minutes, secondary = seconds
            (main * 60 + secondaryPr).toBigDecimal()
        }
        else -> {
            // If it is already in raw seconds or a non-temporal metric, map the principal scalar directly
            main.toBigDecimal()
        }
    }
}

/**
 * Expands a flat, serialized temporal database entry (total seconds) back into a granular [Pair]
 * of displayable string sequences tailored for structural split UI pickers.
 *
 * @param value The raw accumulated metric [BigDecimal] extracted from persistent entity layers.
 * @param unit The target [UnitHabit] profiling blueprint.
 * @return A [Pair] containing the formatted principal value (e.g., hours) and padded secondary value (e.g., minutes).
 */
fun convertFromSeconds(
    value: BigDecimal,
    unit: UnitHabit
): Pair<String, String> {
    val totalSeconds = value.toInt()

    return when (unit) {
        UnitHabit.HOURS -> {
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            Pair(hours.toString(), minutes.toString().padStart(2, '0'))
        }
        UnitHabit.MINUTES -> {
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            Pair(minutes.toString(), seconds.toString().padStart(2, '0'))
        }
        else -> {
            // For direct metrics, return the absolute flat sequence in the first index and clear the secondary layout
            Pair(totalSeconds.toString(), "00")
        }
    }
}