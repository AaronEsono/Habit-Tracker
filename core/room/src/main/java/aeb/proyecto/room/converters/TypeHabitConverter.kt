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

/**
 * Advanced monolithic persistence bridge managing the polymorphic serialization of [TypeHabit].
 *
 * This converter configures a specialized isolation instance of [Gson] injected with custom structural
 * type adapters. It ensures that complex domain-specific calendar objects and polymorphic sealed class
 * hierarchies are marshaled into perfectly flat, standardized JSON strings within the SQLite data block.
 */
class TypeHabitConverter {

    // Custom structural execution instance configured with specialized type adapters
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(TypeHabit::class.java, TypeHabitAdapter())
        .create()

    /**
     * Serializes any polymorphic concrete instance of [TypeHabit] into a structured, flat JSON string layout.
     *
     * @param type The active behavioral cadence model present in memory.
     * @return A clean, flat JSON string representation tracking all underlying metadata.
     */
    @TypeConverter
    fun fromTypeHabit(type:TypeHabit): String {
        return gson.toJson(type)
    }

    /**
     * De-serializes a structural JSON string layout back into its safe, polymorphic [TypeHabit] runtime instance.
     *
     * @param json The raw flat JSON text string sequence extracted from the database row.
     * @return A fully hydrated type-safe [TypeHabit] subclass entity.
     */
    @TypeConverter
    fun toTypeHabit(json: String): TypeHabit {
        val type = object : TypeToken<TypeHabit>() {}.type
        return gson.fromJson(json, type)
    }
}

/**
 * Custom Gson adapter intercepting [LocalDate] streams to enforce flat serialization formatting.
 *
 * Encodes timestamps strictly into the standardized ISO-8601 calendar string layout ("YYYY-MM-DD"),
 * completely bypassing verbose platform-specific reflection overhead.
 */
class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE // Format: "YYYY-MM-DD"

    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        return LocalDate.parse(json?.asString, formatter)
    }
}

/**
 * Custom polymorphic Gson adapter managing the manual structural serialization mapping for the [TypeHabit] hierarchy.
 *
 * It captures concrete sealed properties at runtime and explicitly structures custom [JsonObject] layouts
 * appended with an immutable "tag" discriminator. During inversion, it scans the structural token
 * to parse and reinstantiate the correct type-safe subclass container.
 */
class TypeHabitAdapter : JsonSerializer<TypeHabit>, JsonDeserializer<TypeHabit> {

    /**
     * Marshals polymorphic data properties by flattening state targets into custom JSON objects.
     */
    override fun serialize(src: TypeHabit?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("tag", src?.tag)

        when (src) {
            is TypeHabit.Daily -> {}
            is TypeHabit.Weekly -> {
                jsonObject.addProperty("numberDays", src.numberDays)
                jsonObject.addProperty("weeklyGoal", src.weeklyGoal)
            }
            is TypeHabit.Monthly -> {
                jsonObject.addProperty("numberTimes", src.numberTimes)
                jsonObject.addProperty("monthlyGoal", src.monthlyGoal)
            }
            is TypeHabit.Recurring -> {
                jsonObject.addProperty("date", src.date.toString())
                jsonObject.addProperty("interval", src.interval)
            }
            null -> TypeHabit.Daily
        }
        return jsonObject
    }

    /**
     * Inspects the explicit structural tag identifier to unpack metadata and build type-safe runtime instances.
     */
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TypeHabit {
        val jsonObject = json?.asJsonObject
        val type = jsonObject?.get("tag")?.asString

        return when (type) {
            DAILY_TAG -> TypeHabit.Daily
            WEEKLY_TAG -> TypeHabit.Weekly(
                numberDays = jsonObject.get("numberDays")?.asInt ?: 1,
                weeklyGoal = jsonObject.get("weeklyGoal")?.asBoolean ?: false
            )
            MONTHLY_TAG -> TypeHabit.Monthly(
                numberTimes = jsonObject.get("numberTimes")?.asInt ?: 1,
                monthlyGoal = jsonObject.get("monthlyGoal")?.asBoolean ?: false
            )
            RECURRING_TAG -> TypeHabit.Recurring(
                date = LocalDate.parse(jsonObject.get("date")?.asString),
                interval = jsonObject.get("interval")?.asInt ?: 1
            )
            else -> TypeHabit.Daily
        }
    }
}