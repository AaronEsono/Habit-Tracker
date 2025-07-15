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

class ManageDialogTimerUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    @OptIn(ExperimentalCoroutinesApi::class)
    val showDialogTimer: Flow<ShowDialogState> = datastoreInterface.timerLinkedAndFinished
        .flatMapLatest<Boolean, ShowDialogState> {dialogState ->
            if (!dialogState) {
                flowOf(ShowDialogState.NoShowDialog)
            } else {
                flow {
                    val id = datastoreInterface.getIdTimerSelected() ?: 0L
                    val date = try {
                        LocalDate.parse(datastoreInterface.getDateTimerSelected())
                    } catch (e: Exception) {
                        LocalDate.now()
                    }

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




sealed class ShowDialogState(){
    data object NoShowDialog:ShowDialogState()
    data class ShowDialog(
        val habit: HabitWithDay,
        val time: Long
    ):ShowDialogState()
}