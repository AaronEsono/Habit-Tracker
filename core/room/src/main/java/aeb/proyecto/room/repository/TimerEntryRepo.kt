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

/**
 * Domain repository orchestrating workflows, intelligent historical routing,
 * and state consolidation for time tracking mechanisms.
 *
 * This component coordinates execution queries across multiple chronometric properties
 * (Stopwatches, Countdowns, and Intervals), implementing automatic upsert loops
 * and structural stream combinations to feed dashboard interfaces.
 *
 * @property timerEntryDao Core Data Access Object managing localized timer database records.
 */
class TimerEntryRepo @Inject constructor(
    private val timerEntryDao: TimerEntryDao
){

    /**
     * Resolves, synchronizes, and logs active tracking configurations using a dynamic Upsert pattern.
     *
     * Bypasses processing for unlinked/global stopwatches to optimize database scalability. For valid
     * configurations, it executes a smart lookup: if a matching template exists, it updates its
     * interaction timeline; otherwise, it records a brand new operational profile.
     *
     * @param timeEntry The transient execution snapshot container to evaluate and persist.
     */
    fun findTimeEntry(
        // Safe check: Exclude global, unlinked stopwatches from persistent storage overhead
        timeEntry: TimeEntry
    ) {
        if(!(timeEntry.typeTimer == 0 && timeEntry.idHabit == null)){

            // Route dynamic lookups matching structural dimensions
            val timeEntryBBDD:TimeEntry? = when(timeEntry.typeTimer){
                0 -> { // STOPWATCH
                    timerEntryDao.findStopWatch(timeEntry.idHabit)
                }
                1 -> { // TIMER
                    timerEntryDao.findTimer( timeEntry.time ?: 0L,timeEntry.idHabit)
                }
                else -> { // INTERVAL / TABATA
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
                // Target found: Update operational timestamp and execute synchronization loop
                newTimeEntry = timeEntryBBDD.copy(lastTimeUsed = LocalDateTime.now())
                timerEntryDao.updateTimerEntry(newTimeEntry)
            }else{
                // Target missing: Construct a baseline template configuration record and insert
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

    /**
     * Synthesizes and streams a consolidated collection combining bookmarked favorites
     * alongside the most recently accessed tracking modules.
     *
     * Reduces view-model overhead by binding multiple localized table listeners into
     * a single, reactive dashboard payload timeline.
     *
     * @return A cold [Flow] emitting the aggregated tracking history index array.
     */
    fun getHistoryEntries(): Flow<List<TimeEntryWithHabit>> {
        return combine(
            timerEntryDao.findFavourites(),
            timerEntryDao.findLastTimeEntryUsed()
        ){ favourites, lastUsed ->
            favourites + lastUsed
        }
    }

    /**
     * Toggles bookmark favorite flags and updates usage milestones for an isolated time tracking instrument.
     *
     * @param id The unique target primary key handle to modify.
     * @param favorite The updated boolean flag reflecting user bookmark states.
     */
    fun changeFavorite(id:Long,favorite:Boolean){
        timerEntryDao.updateFavoriteFromTimeEntry(id,favorite)
    }

    /**
     * Wipes an isolated timer configuration row out of the system repository cache.
     *
     * @param id The target unique primary key row handle to drop.
     */
    fun deleteTimeEntry(id:Long){
        timerEntryDao.deleteTimeEntry(id)
    }

}