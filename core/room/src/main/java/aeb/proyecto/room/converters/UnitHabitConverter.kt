package aeb.proyecto.room.converters

import aeb.proyecto.room.model.classes.UnitHabit
import androidx.room.TypeConverter

/**
 * Type-safe persistence registry bridge converting rich metrics [UnitHabit] enums
 * into flat scalar string tokens.
 *
 * Leverages native Java/Kotlin serialization signatures (.name and .valueOf) to guarantee
 * seamless, low-overhead database mapping loops.
 */
class UnitHabitConverter {

    /**
     * Serializes an active [UnitHabit] enum constant into its unique canonical [String] signature.
     *
     * @param unitHabit The rich quantitative measurement unit present in memory.
     * @return The flat string token representation identifier
     */
    @TypeConverter
    fun fromUnitHabit(unitHabit: UnitHabit): String {
        return unitHabit.name
    }

    /**
     * De-serializes a scalar string token back into its validated, type-safe [UnitHabit] rich enum instance.
     *
     * Features a defensive fallback mechanism to prevent catastrophic crashes during schema migrations
     * or structural enum refactoring tasks.
     *
     * @param name The raw scalar string identifier extracted from the persistent database layer.
     * @return The type [UnitHabit] token
     */
    @TypeConverter
    fun toUnitHabit(name: String): UnitHabit {
        return UnitHabit.valueOf(name)
    }
}