package aeb.proyecto.ui.month

import aeb.proyecto.ui.R
import androidx.annotation.StringRes

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

fun getMonth(monthValue: Int): Int {
    return EnumMonths.entries.find { month -> month.value == monthValue }?.title ?: R.string.month_1
}

fun getAvrMonth(month:Int):Int{
    return MonthsAvr.entries.find { it.id == month }?.string ?: R.string.add_habit_jan
}