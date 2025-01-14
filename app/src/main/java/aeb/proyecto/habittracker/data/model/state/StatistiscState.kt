package aeb.proyecto.habittracker.data.model.state

import java.time.LocalDate

data class StatisticsState(
    val timesCompleted:Int = 0,
    val actualStreak:Int = 0,
    val bestStreak:Int = 0
)

data class BestStreak(
    val times:Int = 0,
    val startDate:LocalDate = LocalDate.now(),
    val endDate:LocalDate = LocalDate.now()
)

data class DaysCompleted(
    var completed:Int = 0,
    var uncompleted:Int = 0,
    var noDone:Int = 0
)