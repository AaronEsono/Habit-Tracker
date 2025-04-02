package aeb.proyecto.room.converters

import aeb.proyecto.room.model.classes.TypeNotification
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeNotificationConverter {
    private val gson = Gson() // Use a single instance of Gson

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

    @TypeConverter
    fun toTypeNotification(value: String): TypeNotification {
        return when {
            value.startsWith("daily:") -> {
                val json = value.removePrefix("daily:")
                val listType = object : TypeToken<List<Int>>() {}.type
                val days: List<Int> = try {
                    gson.fromJson(json, listType) ?: emptyList()
                } catch (e: Exception) {
                    emptyList() // Handle JSON parsing errors gracefully
                }
                TypeNotification.Daily(days)
            }
            value.startsWith("recurring:") -> {
                val interval = value.removePrefix("recurring:").toIntOrNull() ?: 1
                TypeNotification.Recurring(interval)
            }
            else -> throw IllegalArgumentException("Unknown TypeNotification format: $value")
        }
    }
}