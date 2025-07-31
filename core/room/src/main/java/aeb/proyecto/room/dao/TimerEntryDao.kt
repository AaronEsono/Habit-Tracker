package aeb.proyecto.room.dao

import aeb.proyecto.room.entities.TimeEntry
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerEntryDao {

    @Insert
    fun insertTimerEntry(timerEntry: TimeEntry): Long

    @Update
    fun updateTimerEntry(timerEntry: TimeEntry)

    @Query(
        """
        Select * from TIMEENTRY where typeTimer = 0
              AND (
            (:idHabit IS NULL AND idHabit IS NULL) OR 
            (idHabit = :idHabit)
          )
            """
    )
    fun findStopWatch(idHabit: Long?): TimeEntry?

    @Query(
        """
    SELECT * FROM TIMEENTRY 
    WHERE typeTimer = 1 
      AND time = :time 
      AND (
            (:idHabit IS NULL AND idHabit IS NULL) OR 
            (idHabit = :idHabit)
          )
    LIMIT 1
    """
    )
    fun findTimer(time: Long, idHabit: Long?): TimeEntry?

    @Query(
        """
        Select * from TIMEENTRY where typeTimer = 2      
            AND (
            (:idHabit IS NULL AND idHabit IS NULL) OR 
            (idHabit = :idHabit)
            ) 
            AND time = :time 
            AND restTime = :rest 
            AND intervals = :interval LIMIT 1
    """
    )
    fun findInterval(time: Long, rest: Long, interval: Long, idHabit: Long?): TimeEntry?

    @Query(
        """
            SELECT 
                -- TimeEntry fields
                TIMEENTRY.id AS timeEntry_id,
                TIMEENTRY.typeTimer AS timeEntry_typeTimer,
                TIMEENTRY.time AS timeEntry_time,
                TIMEENTRY.restTime AS timeEntry_restTime,
                TIMEENTRY.intervals AS timeEntry_intervals,
                TIMEENTRY.idHabit AS timeEntry_idHabit,
                TIMEENTRY.lastTimeUsed AS timeEntry_lastTimeUsed,
                TIMEENTRY.favourite AS timeEntry_favourite,
        
                -- Habit fields
                HABIT.id AS habit_id,
                HABIT.name AS habit_name,
                HABIT.description AS habit_description,
                HABIT.color AS habit_color,
                HABIT.icon AS habit_icon,
                HABIT.goal AS habit_goal,
                HABIT.unit AS habit_unit,
                HABIT.typeHabit AS habit_typeHabit
        
            FROM TIMEENTRY 
            LEFT JOIN HABIT ON TIMEENTRY.idHabit = HABIT.id
            WHERE favourite = 1
            ORDER BY TIMEENTRY.lastTimeUsed DESC
    """
    )
    fun findFavourites(): Flow<List<TimeEntryWithHabit>>

    @Query(
        """
            SELECT 
                -- TimeEntry fields
                TIMEENTRY.id AS timeEntry_id,
                TIMEENTRY.typeTimer AS timeEntry_typeTimer,
                TIMEENTRY.time AS timeEntry_time,
                TIMEENTRY.restTime AS timeEntry_restTime,
                TIMEENTRY.intervals AS timeEntry_intervals,
                TIMEENTRY.idHabit AS timeEntry_idHabit,
                TIMEENTRY.lastTimeUsed AS timeEntry_lastTimeUsed,
                TIMEENTRY.favourite AS timeEntry_favourite,
        
                -- Habit fields
                HABIT.id AS habit_id,
                HABIT.name AS habit_name,
                HABIT.description AS habit_description,
                HABIT.color AS habit_color,
                HABIT.icon AS habit_icon,
                HABIT.goal AS habit_goal,
                HABIT.unit AS habit_unit,
                HABIT.typeHabit AS habit_typeHabit
        
            FROM TIMEENTRY 
            LEFT JOIN HABIT ON TIMEENTRY.idHabit = HABIT.id
            WHERE favourite = 0
            ORDER BY TIMEENTRY.lastTimeUsed DESC
            LIMIT 10
    """
    )
    fun findLastTimeEntryUsed(): Flow<List<TimeEntryWithHabit>>

}