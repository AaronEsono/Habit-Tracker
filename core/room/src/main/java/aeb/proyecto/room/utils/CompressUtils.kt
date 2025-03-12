package aeb.proyecto.room.utils

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.model.habitCompressed.DailyHabitCompressed
import aeb.proyecto.room.model.habitCompressed.EntireHabitCompressed
import aeb.proyecto.room.model.habitCompressed.HabitCompressed
import aeb.proyecto.room.model.habitCompressed.NotificationCompressed
import com.google.gson.Gson
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun jsonCompressed(habits:List<EntireHabit>):String{

    val filteredHabitsAndCompressed = habits.map { habit ->
        EntireHabitCompressed(
            habit = HabitCompressed(habit.habit.name, habit.habit.description, habit.habit.color, habit.habit.icon, habit.habit.times, habit.habit.unit),
            dailyHabits = habit.dailyHabits.filter { it.timesDone != 0 }.map { DailyHabitCompressed(it.timesDone, it.date) },
            notifications = habit.notifications.map { NotificationCompressed(it.hour, it.minute) }.toList()
        )
    }

    val toJson = Gson().toJson(filteredHabitsAndCompressed)
    return compressJson(toJson)
}

fun decompressJsonFirestore(compressed: String): List<EntireHabit> {
    val decompressed = decompressJson(compressed)
    val habits = Gson().fromJson(decompressed, Array<EntireHabitCompressed>::class.java).toList()

    val habitsComplete = habits.map { habitCompressed ->
        val description = if(habitCompressed.habit.description.isNullOrEmpty()) null else habitCompressed.habit.description

        EntireHabit(
            habit = Habit(name = habitCompressed.habit.name, description = description, color = habitCompressed.habit.color, icon = habitCompressed.habit.icon, times =  habitCompressed.habit.times, unit =  habitCompressed.habit.unit),
            dailyHabits = habitCompressed.dailyHabits.map { DailyHabit(timesDone = it.timesDone, date =  it.date) }.toMutableList(),
            notifications = habitCompressed.notifications.map { Notification(hour = it.hour, minute =  it.minute) }.toMutableList()
        )
    }

    return habitsComplete
}

fun compressJson(json: String): String {
    val outputStream = ByteArrayOutputStream()
    GZIPOutputStream(outputStream).use { it.write(json.toByteArray(Charsets.UTF_8)) }
    return Base64.getEncoder().encodeToString(outputStream.toByteArray())
}

fun decompressJson(compressed: String): String {
    val compressedBytes = Base64.getDecoder().decode(compressed)
    return GZIPInputStream(ByteArrayInputStream(compressedBytes)).bufferedReader(Charsets.UTF_8).use { it.readText() }
}