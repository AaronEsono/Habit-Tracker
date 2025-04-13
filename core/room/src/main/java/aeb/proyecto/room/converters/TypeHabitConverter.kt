package aeb.proyecto.room.converters

import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TypeHabitConverter {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(TypeHabit::class.java, TypeHabitAdapter())
        .create()

    @TypeConverter
    fun fromTypeHabit(type:TypeHabit): String {
        return gson.toJson(type)
    }

    @TypeConverter
    fun toTypeHabit(json: String): TypeHabit {
        val type = object : TypeToken<TypeHabit>() {}.type
        return gson.fromJson(json, type)
    }
}

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE // Format: "YYYY-MM-DD"

    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        return LocalDate.parse(json?.asString, formatter)
    }
}


class TypeHabitAdapter : JsonSerializer<TypeHabit>, JsonDeserializer<TypeHabit> {

    override fun serialize(src: TypeHabit?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("tag", src?.tag)

        when (src) {
            is TypeHabit.Daily -> {}
            is TypeHabit.Weekly -> {
                jsonObject.addProperty("numberDays", src.numberDays)
            }
            is TypeHabit.Monthly -> {
                jsonObject.addProperty("numberTimes", src.numberTimes)
            }
            is TypeHabit.Recurring -> {
                jsonObject.addProperty("date", src.date.toString())
                jsonObject.addProperty("interval", src.interval)
            }
            null -> TypeHabit.Daily
        }
        return jsonObject
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TypeHabit {
        val jsonObject = json?.asJsonObject
        val type = jsonObject?.get("tag")?.asString

        return when (type) {
            DAILY_TAG -> TypeHabit.Daily
            WEEKLY_TAG -> TypeHabit.Weekly(
                numberDays = jsonObject.get("numberDays")?.asInt ?: 1
            )
            MONTHLY_TAG -> TypeHabit.Monthly(
                numberTimes = jsonObject.get("numberTimes")?.asInt ?: 1
            )
            RECURRING_TAG -> TypeHabit.Recurring(
                date = LocalDate.parse(jsonObject.get("date")?.asString),
                interval = jsonObject.get("interval")?.asInt ?: 1
            )
            else -> TypeHabit.Daily
        }
    }
}