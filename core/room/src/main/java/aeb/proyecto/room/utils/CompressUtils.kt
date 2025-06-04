package aeb.proyecto.room.utils

import aeb.proyecto.room.dto.EntireHabitDTO
import aeb.proyecto.room.dto.convertToDTO
import aeb.proyecto.room.dto.convertToEntireHabit
import aeb.proyecto.room.entities.relations.EntireHabit
import android.util.Log
import com.google.gson.Gson
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun jsonCompressed(habits:List<EntireHabit>):String{
    val entireHabitDTO = habits.map { it.convertToDTO() }
    return compressJson(Gson().toJson(entireHabitDTO))
}

fun decompressJsonFirestore(compressed: String): List<EntireHabit> {
    val decompressJson = decompressJson(compressed)
    val entireHabitDTO = Gson().fromJson(decompressJson, Array<EntireHabitDTO>::class.java).toList()
    return entireHabitDTO.map { it.convertToEntireHabit() }
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