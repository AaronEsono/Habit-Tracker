package aeb.proyecto.alarmmanager.converters

import aeb.proyecto.room.model.classes.TypeNotification
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.DayOfWeek

/**
 * A custom polymorphic Gson adapter tailored for handling the [TypeNotification] sealed class hierarchy.
 *
 * Since native JSON structures lack inherent metadata to map abstract contracts to physical object subclasses
 * during reflex reflection runtime processes, this adapter injects a string discriminator property named `"tag"`.
 *
 * This explicit polymorphic strategy decouples data streams cleanly based on structural variants:
 * * - **`DAILY`:** Serializes specific active days array maps mapped directly to Java [DayOfWeek] enumerations.
 * - **`RECURRING`:** Serializes local integer intervals representing fixed scheduling frequencies.
 *
 * The deserializer validates structural boundaries dynamically, enforcing key property constraints
 * via [JsonParseException] while offering robust safe-recovery states if unknown variations leak into
 * persistent storage.
 */
class TypeNotificationAdapter : JsonSerializer<TypeNotification>, JsonDeserializer<TypeNotification> {
    override fun serialize(src: TypeNotification?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("tag", src?.tag)

        when (src) {
            is TypeNotification.Daily -> {
                val days = src.days.map { it.name }
                jsonObject.add("days", context.serialize(days))
            }
            is TypeNotification.Recurring -> {
                jsonObject.addProperty("interval", src.interval)
            }
            else -> {}
        }

        return jsonObject
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext): TypeNotification {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON for TypeNotification")
        val type = jsonObject.get("tag")?.asString ?: throw JsonParseException("Missing 'tag' field")

        return when (type) {
            "DAILY" -> {
                val daysArray = jsonObject.getAsJsonArray("days")
                val days = daysArray?.map { DayOfWeek.valueOf(it.asString) } ?: listOf(DayOfWeek.MONDAY)
                TypeNotification.Daily(days)
            }
            "RECURRING" -> TypeNotification.Recurring(jsonObject.get("interval")?.asInt ?: 1)
            else -> TypeNotification.Daily(listOf(DayOfWeek.MONDAY))
        }
    }
}