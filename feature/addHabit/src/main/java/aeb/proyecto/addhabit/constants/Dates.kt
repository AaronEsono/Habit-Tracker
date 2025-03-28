package aeb.proyecto.addhabit.constants

import aeb.proyecto.addhabit.R
import androidx.annotation.StringRes

enum class DaysWeek(@StringRes val string:Int, val id:Int){
    LUNES(R.string.add_habit_monday,1),
    MARTES(R.string.add_habit_tuesday,2),
    MIERCOLES(R.string.add_habit_wednesday,3),
    JUEVES(R.string.add_habit_thursday,4),
    VIERNES(R.string.add_habit_friday,5),
    SABADO(R.string.add_habit_saturday,6),
    DOMINGO(R.string.add_habit_sunday,7)
}

enum class DaysWeekAvr(@StringRes val string:Int, val id:Int){
    LUNES(R.string.add_habit_mon,1),
    MARTES(R.string.add_habit_tue,2),
    MIERCOLES(R.string.add_habit_wed,3),
    JUEVES(R.string.add_habit_thu,4),
    VIERNES(R.string.add_habit_fri,5),
    SABADO(R.string.add_habit_sat,6),
    DOMINGO(R.string.add_habit_sun,7)
}

enum class Months(@StringRes val string:Int, val id:Int){
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

fun getDay(dayOfWeek:Int):Int{
    return DaysWeek.entries.find { it.id == dayOfWeek }?.string ?: R.string.add_habit_monday
}

fun getMonth(month:Int):Int{
    return Months.entries.find { it.id == month }?.string ?: R.string.add_habit_jan
}