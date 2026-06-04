package aeb.proyecto.domain.usecase.habit

import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Domain Use Case designed to dynamically extract the complete collection of unique
 * categorical tags or type classifications currently registered across all active habits.
 *
 * Employs Kotlin's native operator overloading to expose a clean functional invocation
 * interface to downstream architectural layers.
 *
 * @property habitWithDailyHabitRepo The operational repository contract handling core habit queries.
 */
class GetTypesOfHabitUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
) {

    /**
     * Executes the reactive retrieval pipeline to query and compile unique habit type strings.
     * Overloads the functional call syntax, allowing execution via direct instanced reference hooks.
     *
     * @return A continuous reactive stream pipeline carrying a distinct deduplicated list of type categories.
     */
    operator fun invoke(): Flow<List<String>> {
        return habitWithDailyHabitRepo.getExistingTypesHabit()
    }
}