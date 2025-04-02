package aeb.proyecto.room.converters

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.TypeConverter

class IconConverter {
    @TypeConverter
    fun fromImageVector(imageVector: ImageVector): String = imageVector.name.split(".")[1]

    @TypeConverter
    fun toColor(value:String): ImageVector {
        val cl = Class.forName("androidx.compose.material.icons.filled.${value}Kt")
        val method = cl.declaredMethods.first()
        return method.invoke(null, Icons.Filled) as ImageVector
    }
}