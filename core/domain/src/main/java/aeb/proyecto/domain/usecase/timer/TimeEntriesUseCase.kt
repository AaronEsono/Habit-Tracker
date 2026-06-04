package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.room.repository.TimerEntryRepo
import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Domain Use Case designed to manage historical time-tracking entries ([TimeEntryWithHabit]).
 * Orchestrates operations to retrieve the focus diary, toggle favorites, expunge logs, and
 * perform deep state rehydration over the global application timer context based on a past session snapshot.
 *
 * Deconstructs raw total duration metrics seamlessly using isolated internal transformation pipelines.
 *
 * @property timeEntriesRepository The operational repository contract handling historical timer session logs.
 * @property datastoreInterface The abstracted data-layer preference storage contract handling active timer configurations.
 */
class TimeEntriesUseCase @Inject constructor(
    private val timeEntriesRepository: TimerEntryRepo,
    private val datastoreInterface: DatastoreInterface
) {

    /**
     * Streams the complete continuous historical ledger of recorded timer sessions unrolled with
     * their parent habit metadata associations.
     *
     * @return A continuous reactive stream carrying a list of rich relational [TimeEntryWithHabit] logs.
     */
    fun getTimeEntries(): Flow<List<TimeEntryWithHabit>> {
        return timeEntriesRepository.getHistoryEntries()
    }

    /**
     * Toggles the high-importance favorite marker string flag over a specific historical tracking entry.
     *
     * @param id The unique database key identifier of the target time entry.
     * @param favorite The targeted bookmark status representation to enforce.
     */
    fun changeFavorite(id:Long,favorite:Boolean){
        timeEntriesRepository.changeFavorite(id,favorite)
    }

    /**
     * Executes an absolute localized purging operation to delete a specific tracking record from disk history.
     *
     * @param id The unique database key identifier of the targeted time entry to drop.
     */
    fun deleteTimeEntry(id:Long){
        timeEntriesRepository.deleteTimeEntry(id)
    }

    /**
     * Orchestrates a multi-slot atomic rehydration sequence to completely overwrite the current application
     * timer configuration properties with the historical blueprint stored inside a past logging entry.
     *
     * Evaluates polymorphic tracking layouts (Standard, Countdown, Intervals) to hydra preferences cleanly.
     *
     * @param timeEntry The rich relational historical log snapshot chosen as the cloning source template.
     * @param triggerForSegmentedTimer A higher-order functional callback hook to inject computed temporal triples downstream.
     */
    suspend fun setDataFromTimeEntry(timeEntry: TimeEntryWithHabit?, triggerForSegmentedTimer: (Triple<Int,Int,Int>) -> Unit){
        if(timeEntry != null){
            when(timeEntry.timeEntry.typeTimer){
               0 -> {
                   setTimerAndHabit(typeTimer = 0,habit = timeEntry.habit)
               }
               1 -> {
                   setTimerAndHabit(typeTimer = 1,habit = timeEntry.habit)
                   setTimer(timeEntry)

                   timeEntry.timeEntry.time?.let { time ->
                       val timeDivided = secondsToHms(time)

                       val timeDividedInt = Triple(
                           timeDivided.first.toInt(),
                           timeDivided.second.toInt(),
                           timeDivided.third.toInt()
                       )
                       triggerForSegmentedTimer(timeDividedInt)
                   }
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

    /**
     * Mutates persistent state boundaries to map interval-specific properties such as target sets
     * and rest durations into standalone preference slots.
     */
    private suspend fun setDataInterval(timeEntry: TimeEntryWithHabit?){
        timeEntry?.timeEntry?.intervals?.let { sets ->
            datastoreInterface.setNumberSetsTimer(sets.toInt())
        }

        timeEntry?.timeEntry?.restTime?.let {
            val timeDivided = secondsToHms(it)

            datastoreInterface.setRestIntervalHourTimer(timeDivided.first.toInt())
            datastoreInterface.setRestIntervalMinuteTimer(timeDivided.second.toInt())
            datastoreInterface.setRestIntervalSecondTimer(timeDivided.third.toInt())
        }
    }

    /**
     * Establishes the foundational functional mode routing parameter alongside the unique habit
     * reference key inside global preferences.
     */
    private suspend fun setTimerAndHabit(typeTimer:Int, habit:Habit?){
        datastoreInterface.setTypeTimerSelected(typeTimer)

        habit?.id?.let { id ->
            datastoreInterface.setIdHabitLinkedTimer(id)
        } ?: datastoreInterface.setIdHabitLinkedTimer(-1L)
    }

    /**
     * Parses and binds standard baseline runtime countdown clock components back into functional wheel parameters.
     */
    private suspend fun setTimer(timeEntry: TimeEntryWithHabit?){
        timeEntry?.timeEntry?.time?.let { time ->
            val timeDivided = secondsToHms(time)

            datastoreInterface.setHourWheelTimer(timeDivided.first.toInt())
            datastoreInterface.setMinuteWheelTimer(timeDivided.second.toInt())
            datastoreInterface.setSecondWheelTimer(timeDivided.third.toInt())
        }
    }

    /**
     * Mathematical deconstruction engine splitting an accumulated absolute second metric
     * down into structured Hours, Minutes, and Seconds boundaries.
     *
     * @param totalSeconds The total chronological duration represented as a raw long variable.
     * @return A clean domain [Triple] mapped exactly as (Hours, Minutes, Seconds).
     */
    private fun secondsToHms(totalSeconds: Long): Triple<Long, Long, Long> {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return Triple(hours, minutes, seconds)
    }



}