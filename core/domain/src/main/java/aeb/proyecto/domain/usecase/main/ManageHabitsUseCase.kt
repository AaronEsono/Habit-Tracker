package aeb.proyecto.domain.usecase.main

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

/**
 * Domain Use Case designed to orchestrate state mutations and progress accumulation updates
 * over daily habit execution logs ([HabitDay]).
 *
 * Implements an intelligent "Upsert" data branch strategy: automatically evaluates the existence
 * of a localized daily tracking node to seamlessly execute either a delta incrementation or
 * a baseline initialization sequence.
 *
 * @property habitWithDailyHabitRepo The operational repository contract bridging relational habit-progress schemas.
 */
class ManageHabitsUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    /**
     * Executes an atomic progress increment transaction for a specific habit on a given calendar date.
     * Guarantees transactional precision by scaling input parameters into decimal metrics.
     *
     * @param id The unique database key identifier of the targeted parent habit.
     * @param date The specific target calendar date marking the execution window.
     * @param unit The raw amount of progress units accumulated during the tracking session.
     */
    fun updateHabit(id:Long,date:LocalDate,unit:Long) {
        // Conduct a synchronous lookup to extract an existing daily execution token if available
        var dailyHabit = habitWithDailyHabitRepo.getHabitDay(date,id)
        var finalDailyHabit: HabitDay

        if(dailyHabit != null){
            // Branch A: Perform an in-place structural update by accumulating progress via precise arithmetic
            finalDailyHabit = dailyHabit.copy(
                goalDone = dailyHabit.goalDone.plus(unit.toBigDecimal()),
                hourFinishDate = LocalTime.now()
            )

            habitWithDailyHabitRepo.updateDailyHabit(finalDailyHabit)
        }
        else{
            // Branch B: Initialize and insert a brand-new historical completion record for this calendar node
            finalDailyHabit = HabitDay(
                idHabit = id,
                date = date,
                goalDone = unit.toBigDecimal(),
                hourFinishDate = LocalTime.now()
            )

            habitWithDailyHabitRepo.insertDailyHabit(finalDailyHabit)
        }
    }
}