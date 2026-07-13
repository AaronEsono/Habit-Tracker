package aeb.proyecto.room.builder

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.HabitNotification
import android.util.Log

fun habitBuilder(numberHabits:Int):List<Habit>{
    val listHabits = mutableListOf<Habit>()

    for(i in 1..numberHabits){
        val habit = Habit(id = i.toLong())
        listHabits.add(habit)
    }

    return listHabits
}

fun dailyHabitBuilder(numberDailyHabits:Int):List<HabitDay>{
    val listDailyHabits = mutableListOf<HabitDay>()

    for(i in 1..numberDailyHabits){
        val habitDay = HabitDay(id = i.toLong(), idHabit = 1)
        listDailyHabits.add(habitDay)
    }

    return listDailyHabits
}

fun notificationBuilder(numberNotifications:Int):List<HabitNotification>{
    val listNotifications = mutableListOf<HabitNotification>()

    for(i in 1..numberNotifications){
        val notification = HabitNotification(id = i.toLong(), habitId = 1)
        listNotifications.add(notification)
    }

    return listNotifications
}