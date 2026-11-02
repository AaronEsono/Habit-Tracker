package aeb.proyecto.room.converters

import aeb.proyecto.room.model.classes.TypeNotification
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.time.DayOfWeek
import java.time.LocalDate

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
                "recurring:${type.interval}:${type.startDate}"
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

                val days: List<DayOfWeek> = try {
                    gson.fromJson(
                        json,
                        Array<DayOfWeek>::class.java
                    )?.toList() ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }

                TypeNotification.Daily(days)
            }

            value.startsWith("recurring:") -> {
                val parts = value.removePrefix("recurring:")
                    .split(":", limit = 2)

                val interval = parts[0].toIntOrNull() ?: 1

                val startDate = try {
                    parts.getOrNull(1)
                        ?.let(LocalDate::parse)
                        ?: LocalDate.now()
                } catch (e: Exception) {
                    LocalDate.now()
                }

                TypeNotification.Recurring(
                    interval = interval,
                    startDate = startDate
                )
            }

            else -> {
                TypeNotification.Daily(emptyList())
            }
        }
    }
}