package aeb.proyecto.room.converters

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.stripTrailingZeros()?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? =
        value?.toBigDecimalOrNull()
}