package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/**
 * Domain Use Case driving the contextual persistence profile for the dashboard view hierarchy.
 * Manages active filter state parameters, dynamic layout settings, and orchestrates the complex
 * transactional data-handshake required to bridge a habit execution node over to the countdown timer subsystem.
 *
 * Ensure high-precision arithmetic boundaries by utilizing [BigDecimal] during temporal parsing sequences.
 *
 * @property datastore The abstracted data-layer preference storage contract.
 */
class HabitDatastoreUseCase @Inject constructor(
    private val datastore: DatastoreInterface
) {

    /**
     * Continuous reactive data pipeline streaming the user's preferred start day of the week.
     * Maps underlying persistence primitives dynamically into strongly-typed domain [DayOfWeek] tokens.
     */
    val startDayOfWeek: Flow<DayOfWeek?> = datastore.dayOfWeek
        .map {day -> DayOfWeek.valueOf(day) }

    /**
     * Commits the actively selected filter category tag into persistent preferences.
     *
     * @param tag The category string identifier used to slice dashboard view models.
     */
    suspend fun setSelectedHabitType(tag: String) {
        datastore.setTypeSelected(tag)
    }

    /**
     * Resolves the current user-selected categorical tag filter applied to the main layout.
     *
     * @return The active category classification marker string, or null if no filter is enforced.
     */
    suspend fun getTypeSelected(): String? {
        return datastore.getTypeSelected()
    }

    /**
     * Binds a unique habit identifier alongside an active calendar date to register an active tracking relationship.
     *
     * @param id The unique database key identifier of the target habit.
     * @param date The exact tracking execution date targeted for synchronization.
     */
    suspend fun setHabitLinked(id:Long, date: LocalDate){
        datastore.setIdHabitLinkedTimer(id)
        datastore.setDateHabitLinkedTimer(date.toString())
    }

    /**
     * Orchestrates an atomic, multi-slot transactional mutation to prepare the global application timer context
     * to execute an active habit task.
     * Parses higher-precision remaining runtime metrics into standalone hourly wheel indexes.
     *
     * @param id The unique database key identifier of the parent habit.
     * @param date The tracking execution date targeted for synchronization.
     * @param timeLeft The remaining operational duration sequence represented as a decimal token.
     */
    suspend fun setTimerFromHabit(id:Long,date:LocalDate,timeLeft:BigDecimal){
        // Encapsulate and bind the core structural link references
        datastore.setIdHabitLinkedTimer(id)
        datastore.setDateHabitLinkedTimer(date.toString())

        // Deconstruct the higher-precision decimal value into a distinct operational triple
        val hour = bigDecimalToTriple(timeLeft)
        datastore.setHourWheelTimer(hour.first.toInt())
        datastore.setMinuteWheelTimer(hour.second.toInt())
        datastore.setSecondWheelTimer(hour.third.toInt())

        // Enforce structural operational mode mutation (Mode 1: Habit Bound Countdown)
        datastore.setTypeTimerSelected(1)
    }

    /**
     * Deconstructs a precise high-precision decimal time duration token down into a segmented long triple
     * containing standalone Hours, Minutes, and Seconds.
     *
     * Implements an absolute downward scaling truncation boundary to neutralize creeping fractional drift.
     *
     * @param segundosBigDecimal The total accumulated raw duration value measured strictly in seconds.
     * @return A structured [Triple] mapped out precisely as (Hours, Minutes, Seconds).
     */
    fun bigDecimalToTriple(segundosBigDecimal: BigDecimal): Triple<Long, Long, Long> {
        val totalSeconds = segundosBigDecimal.setScale(0, RoundingMode.DOWN).toLong()

        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return Triple(hours, minutes, seconds)
    }

}
