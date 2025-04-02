package aeb.proyecto.ui.date

import aeb.proyecto.ui.R
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

fun getDay(dayOfWeek:Int):Int{
    return DaysWeek.entries.find { it.id == dayOfWeek }?.string ?: R.string.add_habit_monday
}