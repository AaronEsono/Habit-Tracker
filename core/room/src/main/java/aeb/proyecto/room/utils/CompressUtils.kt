package aeb.proyecto.room.utils

import aeb.proyecto.room.entities.relations.EntireHabit
import com.google.gson.Gson
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun jsonCompressed(habits:List<EntireHabit>):String{
    return compressJson(Gson().toJson(habits))
}

fun decompressJsonFirestore(compressed: String): List<EntireHabit> {
    return Gson().fromJson(decompressJson(compressed), Array<EntireHabit>::class.java).toList()
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