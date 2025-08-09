package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.room.repository.TimerEntryRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimeEntriesUseCase @Inject constructor(
    private val timeEntriesRepository: TimerEntryRepo
) {

    fun getTimeEntries(): Flow<List<TimeEntryWithHabit>> {
        return timeEntriesRepository.getHistoryEntries()
    }

    fun changeFavorite(id:Long,favorite:Boolean){
        timeEntriesRepository.changeFavorite(id,favorite)
    }

    fun deleteTimeEntry(id:Long){
        timeEntriesRepository.deleteTimeEntry(id)
    }

}