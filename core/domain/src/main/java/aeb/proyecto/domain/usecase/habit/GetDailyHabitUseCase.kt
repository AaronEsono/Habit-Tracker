package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/**
 * Domain Use Case driving the operational tracking matrix of the application's primary dashboard.
 * Coordinates time-windowed queries and CRUD transactions over individual completion markers ([HabitDay])
 * and relational habit logs.
 *
 * Designed to power reactive calendar interfaces by streaming targeted state vectors.
 *
 * @property habitWithDailyHabitRepo The operational repository contract bridging relational habit-progress schemas.
 */
class GetDailyHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    /**
     * Streams a collection of habits paired with their historical progression metrics, filtered
     * by a strict chronological time window and categorical tag classification.
     *
     * @param from The inclusive starting date boundary of the requested viewport.
     * @param to The inclusive ending date boundary of the requested viewport.
     * @param tag The semantic filter tag classifying the type of habits to retrieve.
     * @return A continuous reactive stream pipeline carrying the list of combined domain entities.
     */
    fun getDailyHabitsByType(from: LocalDate,to:LocalDate, tag: String): Flow<List<HabitWithDailyHabit>> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateAndType(from, to, tag)
    }

    /**
     * Streams the structural progression profile of a unique habit isolated within a specific date range.
     *
     * @param id The unique database key identifier of the target habit.
     * @param from The inclusive starting date boundary of the evaluation window.
     * @param to The inclusive ending date boundary of the evaluation window.
     * @return A continuous reactive stream pipeline emitting the habit profile or null if unmatched.
     */
    fun getHabitWithDailyHabitsByDate(id:Long,from: LocalDate,to:LocalDate): Flow<HabitWithDailyHabit?> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateToDate(id,from,to)
    }

    /**
     * Executes a synchronous lookup to find a specific completion log corresponding to a single calendar day.
     *
     * @param id The unique database key identifier of the parent habit.
     * @param date The exact calendar date targeted for lookup.
     * @return The specific [HabitDay] transaction token, or null if the habit was not logged on that date.
     */
    fun getDailyHabitByDate(id:Long,date:LocalDate): HabitDay?{
        return habitWithDailyHabitRepo.getHabitDay(date,id)
    }

    /**
     * Persists a new completion log entry ([HabitDay]) representing a successful habit iteration.
     *
     * @param habitDay The structural progress token containing execution metrics and the targeted timestamp.
     */
    fun insertHabitDay(habitDay: HabitDay){
        habitWithDailyHabitRepo.insertDailyHabit(habitDay)
    }

    /**
     * Mutates an existing completion log's structural parameters inside the storage pipeline.
     *
     * @param habitDay The modified structural progress token carrying updated metrics.
     */
    fun updateHabitDay(habitDay: HabitDay){
        habitWithDailyHabitRepo.updateDailyHabit(habitDay)
    }

    /**
     * Hard-deletes a specific day's completion record, reverting the habit status for that date to unexecuted.
     *
     * @param id The unique database key identifier of the parent habit.
     * @param date The exact target calendar date to be evicted from logs.
     */
    fun deleteHabitDay(id:Long,date:LocalDate){
        habitWithDailyHabitRepo.deleteHabitDay(id,date)
    }

    /**
     * Permanently purges a habit structure along with all its historical completion records from local storage.
     *
     * @param id The unique database key identifier of the habit targeted for absolute eviction.
     */
    fun deleteHabit(id:Long){
        habitWithDailyHabitRepo.deleteHabit(id)
    }

}