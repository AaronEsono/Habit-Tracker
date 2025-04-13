package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
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
        datastore.setTypeSelectedDate(tag)
    }

    suspend fun getTypeSelected(): String? {
        return datastore.getTypeSelected()
    }
}
