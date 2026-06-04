package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import javax.inject.Inject

/**
 * Domain Use Case designed to perform sharp, synchronous lookups to extract a single [Habit] entity
 * profile using its unique persistent repository key.
 *
 * Engineered for high-efficiency transactional contexts, such as navigating into a specific
 * habit's structural detail view or hydration of editing forms.
 *
 * @property habitWithDailyHabitRepo The operational repository contract handling core habit entities.
 */
class GetHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
) {

    /**
     * Executes an isolated lookup transaction to fetch the baseline attributes of a specific habit.
     * Returns a pure domain model unlinked from reactive flow wrappers.
     *
     * @param id The unique database key identifier of the target habit.
     * @return The specific strongly-typed [Habit] structural entity.
     */
    fun getHabit(id:Long):Habit{
        return habitWithDailyHabitRepo.getHabit(id)
    }

}