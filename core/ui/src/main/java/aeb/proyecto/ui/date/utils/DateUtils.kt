package aeb.proyecto.ui.date.utils

import aeb.proyecto.ui.R
import aeb.proyecto.ui.date.DaysWeek
import aeb.proyecto.ui.date.DaysWeekAvr
import aeb.proyecto.ui.month.getAvrMonth
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.DayOfWeek
import java.time.LocalDate

fun getDay(dayOfWeek:String):Int{
    return DaysWeek.entries.find { it.id.name == dayOfWeek }?.string ?: DaysWeek.LUNES.string
}

fun getOrderedDays(startDay: DayOfWeek): List<DaysWeekAvr> {
    val allDays = DaysWeekAvr.entries
    val startIndex = allDays.indexOfFirst { it.id == startDay }

    return if (startIndex != -1) {
        allDays.drop(startIndex) + allDays.take(startIndex)
    } else {
        allDays
    }
}

fun getAvr(day: DayOfWeek):Int{
    return DaysWeekAvr.entries.find { it.id == day }?.string ?: DaysWeekAvr.LUNES.string
}

@Composable
fun getTextToday(date: LocalDate):String{
    return when(date){
        LocalDate.now() -> stringResource(R.string.today)
        LocalDate.now().plusDays(1) ->  stringResource(R.string.tomorrow)
        LocalDate.now().minusDays(1) -> stringResource(R.string.yesterday)
        else -> {
            stringResource(
                R.string.habit_action_icon,
                date.dayOfMonth.toString(),
                stringResource(getAvrMonth(date.month.value)),
                date.year.toString()
            )
        }
    }
}