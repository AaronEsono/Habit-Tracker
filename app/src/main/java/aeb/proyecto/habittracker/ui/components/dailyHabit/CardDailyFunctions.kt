package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.numberOfDays
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

fun getIcon(dates: List<DailyHabit>, habit: Habit): ImageVector {
    val daily = dates.find { (LocalDate.parse(it.date)) == LocalDate.now() }

    return if (daily != null && daily.timesDone == habit.times) {
        Icons.Filled.Check
    } else
        Icons.Filled.Add

}

fun getProgress(dates: List<DailyHabit>, habit: Habit): Float {
    val dailyHabit = dates.find { (LocalDate.parse(it.date)) == LocalDate.now() }

    return if (dailyHabit != null) {
        dailyHabit.timesDone / habit.times.toFloat()
    } else {
        0f
    }
}

fun getProgressDaily(daily: List<DailyHabit>, date:LocalDate, habit: Habit): Float {
    val dailyHabit = daily.find { (LocalDate.parse(it.date)) == date }
    var progress:Float

    if (dailyHabit != null) {
        progress = dailyHabit.timesDone.toFloat() / habit.times.toFloat()
    } else {
        progress = 0f
    }

    if (progress >= 1) {
        progress = 1f
    }

    return progress
}

fun getDaysOfWeek(): List<LocalDate> {
    val daysOfWeek = mutableListOf<LocalDate>()
    val firstDay = LocalDate.now().minusDays(numberOfDays.toLong() - 1)

    for (i in 0 until numberOfDays) {
        daysOfWeek.add(firstDay.plusDays(i.toLong()))
    }
    return daysOfWeek
}

fun iconByName(name: String): ImageVector {
    val cl = Class.forName("androidx.compose.material.icons.filled.${name}Kt")
    val method = cl.declaredMethods.first()
    return method.invoke(null, Icons.Filled) as ImageVector
}

fun getDay(date:LocalDate):Int{
    return Constans.DaysWeek.entries.find { date.dayOfWeek.value == it.id }?.day ?: R.string.day_1
}