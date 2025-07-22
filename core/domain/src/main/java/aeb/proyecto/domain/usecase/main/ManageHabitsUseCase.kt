package aeb.proyecto.domain.usecase.main

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class ManageHabitsUseCase @Inject constructor(
    private val habitWithDailyHabitRepo: HabitWithDailyHabitRepo
){

    fun updateHabit(id:Long,date:LocalDate,unit:Long) {
        //Encontramos el dailyHabit
        var dailyHabit = habitWithDailyHabitRepo.getHabitDay(date,id)
        var finalDailyHabit: HabitDay

        if(dailyHabit != null){
            //Actualizamos
            finalDailyHabit = dailyHabit.copy(
                goalDone = dailyHabit.goalDone.plus(unit.toBigDecimal()),
                hourFinishDate = LocalTime.now()
            )

            habitWithDailyHabitRepo.updateDailyHabit(finalDailyHabit)
        }
        else{
            //Insertamos
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