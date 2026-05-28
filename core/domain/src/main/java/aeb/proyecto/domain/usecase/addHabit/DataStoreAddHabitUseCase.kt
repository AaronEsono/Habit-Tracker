package aeb.proyecto.domain.usecase.addHabit

import aeb.proyecto.datastore.DatastoreInterface
import java.time.DayOfWeek
import javax.inject.Inject

class DataStoreAddHabitUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {

    suspend fun getDayOfWeek():DayOfWeek{
        return DayOfWeek.valueOf(datastoreInterface.getAppSettings().dayStartWeek)
    }

}