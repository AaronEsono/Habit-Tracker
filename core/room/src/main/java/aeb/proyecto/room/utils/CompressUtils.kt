package aeb.proyecto.room.utils

import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.habit.Habit
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
    return ""
}

fun decompressJsonFirestore(compressed: String): List<EntireHabit> {
    return listOf()
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