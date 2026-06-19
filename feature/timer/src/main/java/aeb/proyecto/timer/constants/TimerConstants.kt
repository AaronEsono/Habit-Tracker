package aeb.proyecto.timer.constants

import aeb.proyecto.timer.R

/**
 * Pre-formatted list of strings for the Hours picker (00-99).
 */
val hours = (0..99).map { it.toString().padStart(2, '0') }

/**
 * Pre-formatted list of strings for the Minutes picker (00-59).
 */
val minutes = (0..59).map { it.toString().padStart(2, '0') }

/**
 * Pre-formatted list of strings for the Seconds picker (00-59).
 */
val seconds = (0..59).map { it.toString().padStart(2, '0') }

/**
 * Enum defining the time units used in the timer configuration interface.
 *
 * @property label The string resource ID representing the unit name.
 */
enum class TypeUnitDate (val label:Int){
    Hours(R.string.timer_hours), Minutes(R.string.timer_minutes), Seconds(R.string.timer_seconds)
}