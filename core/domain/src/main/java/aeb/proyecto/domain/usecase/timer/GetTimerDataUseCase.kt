package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

/**
 * Advanced Domain Use Case designed to synthesize and flatten fragmented preference vectors
 * into a single, unified reactive state framework tailored for the countdown timer environment.
 *
 * Intercepts low-level asynchronous streams, queries localized relational database records,
 * and maps properties into a definitive [TimerData] state token.
 *
 * @property datastoreInterface The abstracted data-layer preference storage contract handling active timer configurations.
 * @property habitWithDailyHabitRepo The operational repository contract bridging relational habit-progress schemas.
 */
class GetTimerDataUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
) {

    /**
     * Continuous reactive data pipeline that unifies multiple preference tracks using layered combination operators.
     * Hydrates structural references defensively to emit a stable composite [TimerData] profile snapshot.
     */
    val timerData: Flow<TimerData> = combine(
        datastoreInterface.idHabitLinkedTimer,
        datastoreInterface.dateHabitLinkedTimer,
        datastoreInterface.typeTimerSelected,
        combine(
            datastoreInterface.wheelHourSelected,
            datastoreInterface.restHourSelected
        ) { hourSelected,restSelected ->
            Pair(hourSelected,restSelected)
        },
        datastoreInterface.numberSetsTimerSelected
    ){ idHabit, dateHabit, typeHabit, (timeHabit, restHour), setsTimer ->

        // Execute dynamic, safe lookup for active parent habit models matching temporal logs
        val habitWithDay = if (idHabit != null && !dateHabit.isNullOrEmpty()) {
            runCatching {
                habitWithDailyHabitRepo.getHabitWithDay(idHabit, LocalDate.parse(dateHabit))
            }.getOrNull()
        } else null

        TimerData(
            habitWithDay = habitWithDay,
            typeTimer = typeHabit ?: 0,
            hourSelected = getHourFromString(timeHabit),
            restHour = getHourFromString(restHour),
            sets = setsTimer ?: 1
        )
    }
}

/**
 * Unified domain state presentation model carrying the integrated data framework
 * required to initialize and draw the countdown timer overlay.
 *
 * @property habitWithDay The active rich relational habit record bound to this timer block, or null if unlinked.
 * @property typeTimer Integer identifier classifying the targeted operational sequence mode.
 * @property hourSelected High-efficiency integer mapping split as (Hours, Minutes, Seconds) for tracking execution.
 * @property restHour High-efficiency integer mapping split as (Hours, Minutes, Seconds) tailored for interval breaks.
 * @property sets Total operational cycle frequencies enforced over intervals.
 */
class TimerData(
    val habitWithDay: HabitWithDay? = null,
    val typeTimer: Int? = null,
    val hourSelected: Triple<Int,Int,Int>? = null,
    val restHour: Triple<Int,Int,Int>? = null,
    val sets:Int = 1
)

/**
 * Deconstructs a formatted chronological sequence string into a strongly-typed integer triple representation.
 * Encapsulates string-splitting transformations inside safe verification wrappers to block creeping formatting drift.
 *
 * @param hour The raw string representation of chronological time expected as an 'HH:MM:SS' layout.
 * @return A structured [Triple] mapped exactly as (Hours, Minutes, Seconds), or null if parsing fails.
 */
fun getHourFromString(hour: String): Triple<Int, Int, Int>? {
    return runCatching {
        val parts = hour.split(":").map { it.toInt() }
        Triple(parts[0], parts[1], parts[2])
    }.getOrNull()
}