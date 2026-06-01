package aeb.proyecto.room.converters

import aeb.proyecto.room.model.classes.TypeNotification
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek

/**
 * Advanced polymorphic persistence bridge for [TypeNotification] sealed class structures.
 *
 * Implements a high-performance Hybrid Serialization Pattern by prefixing structural tags
 * to handle runtime type erasure, combining flat scalar tokens with isolated JSON sub-arrays.
 */
class TypeNotificationConverter {

    // Single instance allocation to optimize memory footprint during massive entity mapping loops
    private val gson = Gson()

    /**
     * Serializes a polymorphic [TypeNotification] instance into a prefixed string layout.
     *
     * - [TypeNotification.Daily] -> "daily:[JSON_ARRAY_OF_DAYS]"
     * - [TypeNotification.Recurring] -> "recurring:INTERVAL_INTEGER"
     *
     * @param type The runtime alert behavioral pattern configuration.
     * @return A flat string snapshot mapping both the discriminator token and encapsulated arguments.
     */
    @TypeConverter
    fun fromTypeNotification(type: TypeNotification): String {
        return when (type) {
            is TypeNotification.Daily -> {
                "daily:" + gson.toJson(type.days)
            }
            is TypeNotification.Recurring -> {
                "recurring:${type.interval}"
            }
        }
    }

    /**
     * De-serializes a tagged string snapshot back into its safe, polymorphic [TypeNotification] runtime layout.
     *
     * Features graceful recovery configurations to prevent catastrophic runtime exceptions
     * in case of schema structural mismatching.
     *
     * @param value The raw tagged text string sequence extracted from the local database layer.
     * @return A fully hydrated type-safe [TypeNotification] instance, defaulting to an empty Daily setup on parsing failures.
     */
    @TypeConverter
    fun toTypeNotification(value: String): TypeNotification {
        return when {
            value.startsWith("daily:") -> {
                val json = value.removePrefix("daily:")
                val listType = object : TypeToken<List<DayOfWeek>>() {}.type
                val days: List<DayOfWeek> = try {
                    gson.fromJson(json, listType) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
                TypeNotification.Daily(days)
            }
            value.startsWith("recurring:") -> {
                val interval = value.removePrefix("recurring:").toIntOrNull() ?: 1
                TypeNotification.Recurring(interval)
            }
            else -> {
                // Defensive fallback strategy to safeguard release builds against unexpected data shapes
                TypeNotification.Daily(emptyList())
            }
        }
    }
}