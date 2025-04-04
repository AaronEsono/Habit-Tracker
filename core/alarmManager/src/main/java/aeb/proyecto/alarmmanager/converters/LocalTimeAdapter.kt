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

class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.toString()) // Guarda en formato "HH:mm"
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime {
        return json?.asString?.let { LocalTime.parse(it) } ?: LocalTime.MIDNIGHT // Si es `{}`, usa 00:00
    }
}