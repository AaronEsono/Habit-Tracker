package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.room.repository.TimerEntryRepo
import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimeEntriesUseCase @Inject constructor(
    private val timeEntriesRepository: TimerEntryRepo,
    private val datastoreInterface: DatastoreInterface
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

    suspend fun setDataFromTimeEntry(timeEntry: TimeEntryWithHabit?){
        if(timeEntry != null){
            when(timeEntry.timeEntry.typeTimer){
               0 -> {
                   setTimerAndHabit(typeTimer = 0,habit = timeEntry.habit)
               }
               1 -> {
                   setTimerAndHabit(typeTimer = 1,habit = timeEntry.habit)
                   setTimer(timeEntry)
               }
               2 -> {
                   setTimerAndHabit(typeTimer = 2,habit = timeEntry.habit)
                   setTimer(timeEntry)
                   setDataInterval(timeEntry)
               }
               else -> {}
            }
        }
    }

    private suspend fun setDataInterval(timeEntry: TimeEntryWithHabit?){
        timeEntry?.timeEntry?.intervals?.let { sets ->
            datastoreInterface.setSetsTimer(sets.toInt())
        }

        timeEntry?.timeEntry?.restTime?.let {
            val timeDivided = secondsToHms(it)

            datastoreInterface.setRestIntervalHourTimer(timeDivided.first.toInt())
            datastoreInterface.setRestIntervalMinuteTimer(timeDivided.second.toInt())
            datastoreInterface.setRestIntervalSecondTimer(timeDivided.third.toInt())
        }
    }

    private suspend fun setTimerAndHabit(typeTimer:Int, habit:Habit?){
        datastoreInterface.setTypeTimerSelected(typeTimer)

        habit?.id?.let { id ->
            datastoreInterface.setIdTimerSelected(id)
        } ?: datastoreInterface.setIdTimerSelected(-1L)
    }

    private suspend fun setTimer(timeEntry: TimeEntryWithHabit?){
        timeEntry?.timeEntry?.time?.let { time ->
            val timeDivided = secondsToHms(time)

            datastoreInterface.setHourWheelTimer(timeDivided.first.toInt())
            datastoreInterface.setMinuteWheelTimer(timeDivided.second.toInt())
            datastoreInterface.setSecondWheelTimer(timeDivided.third.toInt())
        }
    }

    private fun secondsToHms(totalSeconds: Long): Triple<Long, Long, Long> {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return Triple(hours, minutes, seconds)
    }



}