package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/** Use case del datastore para el viewModel de la pantalla habits
 *  Obtiene el dia se la semana seleccionado por el usuario
 *  y el tipo de habitos seleccionado por el usuario
 *  */
class HabitDatastoreUseCase @Inject constructor(
    private val datastore: DatastoreInterface
) {

    val startDayOfWeek: Flow<DayOfWeek?> = datastore.dayOfWeek
        .map {day -> DayOfWeek.valueOf(day) }

    suspend fun setSelectedHabitType(tag: String) {
        datastore.setTypeSelected(tag)
    }

    suspend fun getTypeSelected(): String? {
        return datastore.getTypeSelected()
    }

    suspend fun setHabitLinked(id:Long, date: LocalDate){
        datastore.setIdTimerSelected(id)
        datastore.setDateTimerSelected(date.toString())
    }

    suspend fun setTimerFromHabit(id:Long,date:LocalDate,timeLeft:BigDecimal){
        //Seteamos el hábito
        datastore.setIdTimerSelected(id)
        datastore.setDateTimerSelected(date.toString())

        //Seteamos la hora
        val hour = bigDecimalToTriple(timeLeft)
        datastore.setHourWheelTimer(hour.first.toInt())
        datastore.setMinuteWheelTimer(hour.second.toInt())
        datastore.setSecondWheelTimer(hour.third.toInt())

        //Setear timer
        datastore.setTypeTimerSelected(1)
    }

    fun bigDecimalToTriple(segundosBigDecimal: BigDecimal): Triple<Long, Long, Long> {
        val totalSeconds = segundosBigDecimal.setScale(0, RoundingMode.DOWN).toLong()

        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return Triple(hours, minutes, seconds)
    }

}
