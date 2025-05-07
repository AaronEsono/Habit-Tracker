package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject


class GetTimerDataUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
) {

    val timerData: Flow<TimerData> = combine(
        datastoreInterface.idTimerSelected,
        datastoreInterface.dateTimerSelected,
        datastoreInterface.typeTimerSelected,
        datastoreInterface.hourSelected,
        datastoreInterface.restHourSelected
    ){ idHabit, dateHabit, typeHabit, timeHabit, restHour ->

        val habitWithDay = if (idHabit != null && !dateHabit.isNullOrEmpty()) {
            runCatching {
                habitWithDailyHabitRepo.getHabitWithDay(idHabit, LocalDate.parse(dateHabit))
            }.getOrNull()
        } else null

        TimerData(
            habitWithDay = habitWithDay,
            typeTimer = typeHabit ?: 0,
            hourSelected = getHourFromString(timeHabit),
            restHour = getHourFromString(restHour)
        )
    }
}


class TimerData(
    val habitWithDay: HabitWithDay? = null,
    val typeTimer: Int? = null,
    val hourSelected: Triple<Int,Int,Int>? = null,
    val restHour: Triple<Int,Int,Int>? = null
)

fun getHourFromString(hour: String): Triple<Int, Int, Int>? {
    return runCatching {
        val parts = hour.split(":").map { it.toInt() }
        Triple(parts[0], parts[1], parts[2])
    }.getOrNull()
}