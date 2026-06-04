package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import javax.inject.Inject

/**
 * Domain Use Case designed to extract a specialized sub-profile of habit entities
 * to feed the application's standalone countdown timer configuration interface.
 *
 * Filters out static or tally-based metrics, streaming exclusively those habit structures
 * configured to accumulate and measure progress utilizing temporal metrics (Time Units).
 *
 * @property habitWithDailyHabitRepo The operational repository contract handling core habit queries.
 */
class GetHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    /**
     * Streams a live, continuous collection of habit specifications restricted strictly
     * to time-based operational tracking unit parameters.
     * Used to hydrate context-vetted linking dropdowns inside the countdown system overlay.
     *
     * @return A continuous reactive stream carrying a filtered list of time-bound [Habit] entities.
     */
    fun getAllHabitsWithTimeUnit() = habitWithDailyHabitRepo.getHabitWithTimeUnit()
}