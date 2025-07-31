package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.TimerEntryDao
import aeb.proyecto.room.entities.TimeEntry
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import android.util.Log
import androidx.room.Insert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime
import javax.inject.Inject

class TimerEntryRepo @Inject constructor(
    private val timerEntryDao: TimerEntryDao
){

    fun findTimeEntry(
        timeEntry: TimeEntry
    ) {
        if(!(timeEntry.typeTimer == 0 && timeEntry.idHabit == null)){

            val timeEntryBBDD:TimeEntry? = when(timeEntry.typeTimer){
                0 -> {
                    timerEntryDao.findStopWatch(timeEntry.idHabit)
                }
                1 -> {
                    timerEntryDao.findTimer( timeEntry.time ?: 0L,timeEntry.idHabit)
                }
                else -> {
                    timerEntryDao.findInterval(
                        timeEntry.time ?: 0L,
                        timeEntry.restTime ?: 0L,
                        timeEntry.intervals ?: 0,
                        timeEntry.idHabit
                    )
                }
            }

            val newTimeEntry:TimeEntry

            if(timeEntryBBDD != null){
                newTimeEntry = timeEntryBBDD.copy(lastTimeUsed = LocalDateTime.now())

                timerEntryDao.updateTimerEntry(newTimeEntry)
            }else{
                newTimeEntry = TimeEntry(
                    typeTimer = timeEntry.typeTimer,
                    idHabit = timeEntry.idHabit,
                    time = timeEntry.time,
                    restTime = timeEntry.restTime,
                    intervals = timeEntry.intervals,
                    lastTimeUsed = LocalDateTime.now()
                )

                timerEntryDao.insertTimerEntry(newTimeEntry)
            }
        }
    }

    fun getHistoryEntries(): Flow<List<TimeEntryWithHabit>> {
        return combine(
            timerEntryDao.findFavourites(),
            timerEntryDao.findLastTimeEntryUsed()
        ){ favourites, lastUsed ->
            favourites + lastUsed
        }
    }

}