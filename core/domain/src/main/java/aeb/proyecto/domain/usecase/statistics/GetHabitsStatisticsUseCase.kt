package aeb.proyecto.domain.usecase.statistics

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Domain Use Case designed to pull historical, relational time-series aggregates
 * from local persistence layers to hydrate analytical charts and progress dashboards.
 *
 * Provides variable granularity queries tracking absolute lifetimes down to precise calendar
 * bounding windows ([LocalDate]) to optimize data payload processing.
 *
 * @property habitWithDailyHabitRepo The operational repository contract bridging relational habit-progress schemas.
 */
class GetHabitsStatisticsUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    /**
     * Extracts a live stream of all basic habit metadata specifications registered in the database.
     * Typically utilized to populate interactive picker menus or filter selectors inside statistics panels.
     *
     * @return A continuous reactive stream carrying a list of baseline [Habit] specifications.
     */
    fun getAllHabits(): Flow<List<Habit>> {
        return habitWithDailyHabitRepo.getAllHabits()
    }

    /**
     * Resolves the entire historical execution lifetime logging profile for a specific targeted habit.
     * Useful to calculate grand total metrics, global percentages, and record-breaking streaks.
     *
     * @param id The unique database key identifier of the parent habit.
     * @return A continuous reactive stream carrying the aggregate model with all historical log entries.
     */
    fun getHabitWithDailyHabit(id:Long): Flow<HabitWithDailyHabit?> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabit(id)
    }

    /**
     * Queries and isolates habit execution log structures tightly constrained inside a specific
     * calendar date range window.
     * Highly optimized to feed fixed-interval charts (weekly summaries, monthly trends) without memory bloating.
     *
     * @param id The unique database key identifier of the parent habit.
     * @param from The inclusive starting calendar boundary date vector.
     * @param to The inclusive ending calendar boundary date vector.
     * @return A continuous reactive stream carrying the habit aggregate localized to the requested time frame.
     */
    fun getHabitWithDailyHabitsByDate(id:Long,from: LocalDate,to:LocalDate): Flow<HabitWithDailyHabit?> {
        return habitWithDailyHabitRepo.getHabitWithDailyHabitsByDateToDate(id,from,to)
    }

}