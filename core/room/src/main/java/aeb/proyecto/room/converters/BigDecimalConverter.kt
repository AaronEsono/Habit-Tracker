package aeb.proyecto.room.converters

import androidx.room.TypeConverter
import java.math.BigDecimal

/**
 * Robust persistence marshaller handling lossless serialization of arbitrary-precision decimal metrics.
 *
 * This converter ensures that sensitive quantitative habit goal values are safely converted to flat
 * string structures, completely bypassing floating-point truncation issues inherent to SQLite numerical types.
 */
class BigDecimalConverter {

    /**
     * Serializes a runtime [BigDecimal] instance into a flat, standardized [String] layout.
     *
     * It strips unneeded trailing zeros and enforces flat plain text styling to completely prevent
     * scientific notation anomalies (e.g., matching '1E+2' vs '100') within the SQLite storage block.
     *
     * @param value The raw precision decimal extracted from active memory state layers.
     * @return A sanitized plain-text representation string, or null if the input was unallocated.
     */
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.stripTrailingZeros()?.toPlainString()

    /**
     * De-serializes a flat text sequence stored in the local persistent cache back into a type-safe [BigDecimal].
     *
     * @param value The raw scalar plain-text numerical string sequence.
     * @return A fully hydrated [BigDecimal] precision instance, or null if parsing fails or input was empty.
     */
    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? =
        value?.toBigDecimalOrNull()
}