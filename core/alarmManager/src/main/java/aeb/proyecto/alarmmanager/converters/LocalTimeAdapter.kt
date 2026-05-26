package aeb.proyecto.alarmmanager.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * A custom Gson adapter responsible for the serialization and deserialization of [LocalTime] instances.
 *
 * This adapter uses the standardized [DateTimeFormatter.ISO_LOCAL_TIME] pattern (`HH:mm:ss`) to enforce
 * cross-platform string representations within JSON stores. To prevent runtime parsing exceptions from
 * corrupted or incomplete raw schema definitions, the deserializer implements a safe recovery boundary
 * defaulting directly to [LocalTime.MIDNIGHT].
 */
class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.toString()) // Persists into standard string format
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime {
        return json?.asString?.let { LocalTime.parse(it) } ?: LocalTime.MIDNIGHT // Safe fallback protection
    }
}