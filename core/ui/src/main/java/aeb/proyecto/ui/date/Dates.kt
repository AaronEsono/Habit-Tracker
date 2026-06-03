package aeb.proyecto.ui.date

import aeb.proyecto.ui.R
import androidx.annotation.StringRes
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * Structural localization dictionary mapping the full names of the week days.
 * Pairs platform [DayOfWeek] engine constants directly with descriptive Android string resource
 * pointer tokens to streamline multi-language string resolution inside the UI layers.
 *
 * @property string The compiler-guaranteed [StringRes] structural integer resource pointer.
 * @property id The standard platform [DayOfWeek] biological identifier instance.
 */
enum class DaysWeek(@StringRes val string:Int, val id:DayOfWeek){
    LUNES(R.string.add_habit_monday,DayOfWeek.MONDAY),
    MARTES(R.string.add_habit_tuesday,DayOfWeek.TUESDAY),
    MIERCOLES(R.string.add_habit_wednesday,DayOfWeek.WEDNESDAY),
    JUEVES(R.string.add_habit_thursday,DayOfWeek.THURSDAY),
    VIERNES(R.string.add_habit_friday,DayOfWeek.FRIDAY),
    SABADO(R.string.add_habit_saturday,DayOfWeek.SATURDAY),
    DOMINGO(R.string.add_habit_sunday,DayOfWeek.SUNDAY)
}

/**
 * Specialized high-density abbreviation dictionary mapping localized short tokens (e.g., Mon, Tue, Wed).
 * Engineered explicitly to feed tight visual layout vertices like calendar grid headers [CalendarDays]
 * without generating text-wrapping distortions across narrow mobile screen sizes.
 *
 * @property string The compiler-guaranteed [StringRes] structural integer resource pointer.
 * @property id The standard platform [DayOfWeek] biological identifier instance.
 */
enum class DaysWeekAvr(@StringRes val string:Int, val id:DayOfWeek){
    LUNES(R.string.add_habit_mon,DayOfWeek.MONDAY),
    MARTES(R.string.add_habit_tue,DayOfWeek.TUESDAY),
    MIERCOLES(R.string.add_habit_wed,DayOfWeek.WEDNESDAY),
    JUEVES(R.string.add_habit_thu,DayOfWeek.THURSDAY),
    VIERNES(R.string.add_habit_fri,DayOfWeek.FRIDAY),
    SABADO(R.string.add_habit_sat,DayOfWeek.SATURDAY),
    DOMINGO(R.string.add_habit_sun,DayOfWeek.SUNDAY)
}