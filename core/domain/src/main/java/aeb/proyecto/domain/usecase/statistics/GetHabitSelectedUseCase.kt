package aeb.proyecto.domain.usecase.statistics

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Domain Use Case designed to orchestrate and stream contextual filter parameters required to
 * hydrate historical performance charts and metrics inside the statistics view hierarchy.
 *
 * Tracks active focused selections across device preference pipelines to ensure persistent user view states.
 *
 * @property datastore The abstracted data-layer preference storage contract handling active analysis targets.
 */
class GetHabitSelectedUseCase @Inject constructor(
    private val datastore: DatastoreInterface
) {

    /**
     * Streams the unique persistent identifier of the habit currently focused for statistical decomposition.
     * Emits null if the user clears filters to evaluate global or consolidated app metrics.
     *
     * @return A continuous reactive stream pipeline carrying the selected habit key identifier.
     */
    fun getHabitSelected(): Flow<Long?>{
        return datastore.habitSelected
    }

    /**
     * Commits a new target habit identifier into preferences to update active charting focus points across view models.
     *
     * @param id The unique database key identifier of the habit selected for analytical breakdown.
     */
    suspend fun setHabitSelected(id:Long){
        datastore.setHabitSelected(id)
    }

    /**
     * Streams the baseline day designation framework currently enforced by the active profile settings.
     * Essential to align chart horizontal axes coordinate points with the user's localized calendar matrix.
     *
     * @return A continuous reactive stream pipeline carrying the preferred starting day string token.
     */
    fun getDaySelected(): Flow<String?>{
        return datastore.dayOfWeek
    }

}