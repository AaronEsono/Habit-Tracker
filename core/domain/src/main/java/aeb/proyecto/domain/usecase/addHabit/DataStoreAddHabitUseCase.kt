package aeb.proyecto.domain.usecase.addHabit

import aeb.proyecto.datastore.DatastoreInterface
import java.time.DayOfWeek
import javax.inject.Inject

/**
 * Domain Use Case designed to extract specific user preferences from the local storage pipeline
 * required during the creation sequence of a new habit.
 *
 * This component acts as a framework-agnostic bridge, abstracting DataStore IO interactions
 * behind a clean domain contract interface.
 *
 * @property datastoreInterface The abstracted data-layer boundary contract handling local app settings.
 */
class DataStoreAddHabitUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {

    /**
     * Retrieves the configured starting day of the week from the active user profile preferences.
     * Maps the persistent data primitive into a robust, strongly-typed [DayOfWeek] domain model.
     *
     * @return The functional [DayOfWeek] instance representing the baseline for calendar calculations.
     */
    suspend fun getDayOfWeek():DayOfWeek{
        return runCatching {
            DayOfWeek.valueOf(datastoreInterface.getAppSettings().dayStartWeek)
        }.getOrElse {
            DayOfWeek.MONDAY
        }
    }

}