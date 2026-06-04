package aeb.proyecto.domain.usecase.main

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

/**
 * Advanced Domain Use Case handling the reactive synchronization and orchestration rules when
 * an active habit-bound countdown timer reaches its completion state.
 *
 * Flattens asynchronous state streams from storage, queries local persistence matrices,
 * and maps the output into a definitive, UI-consumable sealed state framework.
 *
 * Forces downstream computations onto the optimal [Dispatchers.IO] boundary context.
 */
class ManageDialogTimerUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    /**
     * Continuous reactive data pipeline evaluating whether to display or evict the post-timer completion overlay.
     * Uses a shifting transformation layout to query and assemble entity details safely on demand.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val showDialogTimer: Flow<ShowDialogState> = datastoreInterface.timerLinkedAndFinished
        .flatMapLatest<Boolean, ShowDialogState> {dialogState ->
            if (!dialogState) {
                flowOf(ShowDialogState.NoShowDialog)
            } else {
                flow {
                    // Extract core metadata references from preference memory safely
                    val id = datastoreInterface.getIdHabitLinkedTimer() ?: 0L

                    // Enforce defensive exception shielding over structural string-to-date parsing sequences
                    val date = try {
                        LocalDate.parse(datastoreInterface.getDateHabitLinkedTimer())
                    } catch (e: Exception) {
                        LocalDate.now()
                    }

                    // Query the underlying Room repository to fetch the localized daily status blueprint
                    val habit = habitWithDailyHabitRepo.getHabitWithDayOrNull(id, date)

                    if (habit != null) {
                        val time = datastoreInterface.getTimePassedTimer() ?: 0L
                        emit(
                            ShowDialogState.ShowDialog(
                                habit = habit,
                                time = time
                            )
                        )
                    } else {
                        emit(ShowDialogState.NoShowDialog)
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
}



/**
 * Sealed architecture layout blueprint defining the valid presentation states
 * for the post-timer completion display interface.
 */
sealed class ShowDialogState(){

    /**
     * Represents a quiet state vector implying the completion overlay must remain hidden.
     * Implemented as an optimized singleton data object to avoid redundant allocations.
     */
    data object NoShowDialog:ShowDialogState()

    /**
     * Represents an active state trigger indicating the completion overlay must be mounted.
     *
     * @property habit The rich relational model carrying the targeted habit alongside its day execution logs.
     * @property time The total chronological duration tracked and spent inside the active timer block.
     */
    data class ShowDialog(
        val habit: HabitWithDay,
        val time: Long
    ):ShowDialogState()
}