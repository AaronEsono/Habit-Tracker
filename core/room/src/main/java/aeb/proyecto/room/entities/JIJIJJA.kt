package aeb.proyecto.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JIJIJJA(
    @PrimaryKey
    val hola:Int = 0,
    @ColumnInfo(defaultValue = "0")
    val prueba:Int = 0,
)