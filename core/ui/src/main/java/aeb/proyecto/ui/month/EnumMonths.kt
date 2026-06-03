package aeb.proyecto.ui.month

import aeb.proyecto.ui.R
import androidx.annotation.StringRes

/**
 * Structural localization dictionary mapping the full names of the calendar months.
 * Pairs standard platform chronological integer identifiers directly with descriptive Android string
 * resource pointer tokens to streamline multi-language string resolution inside top visual headers.
 *
 * @property value The platform chronological month integer footprint (1 = January, 12 = December).
 * @property title The compiler-guaranteed [StringRes] structural integer resource pointer.
 */
enum class EnumMonths (val value:Int,@StringRes val title:Int){
    JANUARY(1,R.string.month_1),
    FEBRUARY(2,R.string.month_2),
    MARCH(3,R.string.month_3),
    APRIL(4,R.string.month_4),
    MAY(5,R.string.month_5),
    JUNE(6,R.string.month_6),
    JULY(7,R.string.month_7),
    AUGUST(8,R.string.month_8),
    SEPTEMBER(9,R.string.month_9),
    OCTOBER(10,R.string.month_10),
    NOVEMBER(11,R.string.month_11),
    DECEMBER(12, R.string.month_12)
}

/**
 * Specialized high-density abbreviation dictionary mapping localized short month tokens (e.g., Jan, Feb, Mar).
 * Engineered explicitly to feed conversational text aggregates like historical timeline tags [getTextToday]
 * without overflowing layout components across narrow device views.
 *
 * @property string The compiler-guaranteed [StringRes] structural integer resource pointer.
 * @property id The platform chronological month integer footprint (1 = January, 12 = December).
 */
enum class MonthsAvr(@StringRes val string:Int, val id:Int){
    JANUARY(R.string.add_habit_jan,1),
    FEBRUARY(R.string.add_habit_feb,2),
    MARCH(R.string.add_habit_mar,3),
    APRIL(R.string.add_habit_apr,4),
    MAY(R.string.add_habit_may,5),
    JUNE(R.string.add_habit_jun,6),
    JULY(R.string.add_habit_jul,7),
    AUGUST(R.string.add_habit_aug,8),
    SEPTEMBER(R.string.add_habit_sep,9),
    OCTOBER(R.string.add_habit_oct,10),
    NOVEMBER(R.string.add_habit_nov,11),
    DECEMBER(R.string.add_habit_dec,12)
}

/**
 * Resolves the localized full-string resource identifier associated with a raw chronological month value.
 * Establishes a defensive boundary fallback to January [R.string.month_1] if the input integer overflows standard limits.
 *
 * @param monthValue The primitive raw month integer index (1 to 12).
 * @return The functional compiler-guaranteed [StringRes] integer pointer.
 */
fun getMonth(monthValue: Int): Int {
    return EnumMonths.entries.find { month -> month.value == monthValue }?.title ?: R.string.month_1
}

/**
 * Resolves the localized abbreviation string resource identifier associated with a raw chronological month value.
 * Establishes a defensive boundary fallback to January [R.string.add_habit_jan] if the input integer leaks out of bounds.
 *
 * @param month The primitive raw month integer index (1 to 12).
 * @return The functional compiler-guaranteed [StringRes] integer pointer.
 */
fun getAvrMonth(month:Int):Int{
    return MonthsAvr.entries.find { it.id == month }?.string ?: R.string.add_habit_jan
}