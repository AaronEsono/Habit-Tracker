package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.utils.Constans.dayOfWeek
import aeb.proyecto.habittracker.utils.Constans.requiredDays
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.Color
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

fun getPrecomputedItems(items:Int, dailyHabits: List<DailyHabit>, habit: Habit):List<Color>{
    val dateMap = dailyHabits.associateBy { LocalDate.parse(it.date) }

    return List(items) { index ->
        val dateDay = LocalDate.now().minusDays((items - index - 1).toLong())
        getColorDay(dateDay, dateMap, habit)
    }
}

fun getColorDay(dateDay: LocalDate, dateMap: Map<LocalDate, DailyHabit>, habit: Habit): Color {
    val dailyHabit = dateMap[dateDay] // O(1) en lugar de O(n) con 'find'

    return if (dailyHabit != null) {
        Color(habit.color).copy(alpha = 0.1f + (1f - 0.1f) * (dailyHabit.timesDone.toFloat() / habit.times.toFloat()))
    } else {
        Color(habit.color).copy(alpha = 0.1f)
    }
}

fun getPrintDays(): Int {
    val todayDay = LocalDate.now().dayOfWeek.value
    val daySelected = dayOfWeek

    val difference = todayDay - daySelected

    return if (difference < 0) {
        8 + difference + requiredDays
    } else {
        difference + 1 + requiredDays
    }
}

fun iconByName(name: String): ImageVector {
    val cl = Class.forName("androidx.compose.material.icons.filled.${name}Kt")
    val method = cl.declaredMethods.first()
    return method.invoke(null, Icons.Filled) as ImageVector
}