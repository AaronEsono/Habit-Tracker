package aeb.proyecto.alarmmanager.converters

import aeb.proyecto.room.model.classes.TypeNotification
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TypeNotificationAdapter : JsonSerializer<TypeNotification>, JsonDeserializer<TypeNotification> {
    override fun serialize(src: TypeNotification?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("tag", src?.tag)

        when (src) {
            is TypeNotification.Daily -> jsonObject.add("days", context.serialize(src.days))
            is TypeNotification.Recurring -> jsonObject.addProperty("interval", src.interval)
            else -> {}
        }

        return jsonObject
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext): TypeNotification {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON for TypeNotification")
        val type = jsonObject.get("tag")?.asString ?: throw JsonParseException("Missing 'tag' field")

        return when (type) {
            "DAILY" -> TypeNotification.Daily(jsonObject.getAsJsonArray("days")?.map { it.asInt } ?: listOf(1))
            "RECURRING" -> TypeNotification.Recurring(jsonObject.get("interval")?.asInt ?: 1)
            else -> TypeNotification.Daily(listOf(1))
        }
    }
}