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

/**
 * Serializes a domain-layer habit ecosystem graph into a highly compressed, transport-safe Base64 string.
 *
 * @param habits The collection of [EntireHabit] relational models to compress.
 * @return A compressed Base64 [String] payload ready for cloud database writes.
 */
fun jsonCompressed(habits:List<EntireHabit>):String{
    val entireHabitDTO = habits.map { it.convertToDTO() }
    return compressJson(Gson().toJson(entireHabitDTO))
}

/**
 * Decodes, decompresses, and deserializes an upstream transport payload back into type-safe domain entities.
 *
 * @param compressed The compressed Base64 [String] payload fetched from the remote registry.
 * @return A fully hydrated collection of relational [EntireHabit] entities ready for local persistence.
 */
fun decompressJsonFirestore(compressed: String): List<EntireHabit> {
    val decompressJson = decompressJson(compressed)
    val entireHabitDTO = Gson().fromJson(decompressJson, Array<EntireHabitDTO>::class.java).toList()
    return entireHabitDTO.map { it.convertToEntireHabit() }
}

/**
 * Compresses a raw string payload using GZIP and encodes the binary matrix into a scalar Base64 layout.
 *
 * @param json The structural JSON string target.
 * @return A flat, web-safe Base64 encoded representation of the compressed stream.
 */
fun compressJson(json: String): String {
    val outputStream = ByteArrayOutputStream()
    GZIPOutputStream(outputStream).use { it.write(json.toByteArray(Charsets.UTF_8)) }
    return Base64.getEncoder().encodeToString(outputStream.toByteArray())
}

/**
 * Decodes a scalar Base64 matrix and inflates the underlying binary payload using a GZIP decompression stream.
 *
 * @param compressed The compressed Base64 string sequence.
 * @return The original, uncompressed raw UTF-8 string layout.
 */
fun decompressJson(compressed: String): String {
    val compressedBytes = Base64.getDecoder().decode(compressed)
    return GZIPInputStream(ByteArrayInputStream(compressedBytes)).bufferedReader(Charsets.UTF_8).use { it.readText() }
}