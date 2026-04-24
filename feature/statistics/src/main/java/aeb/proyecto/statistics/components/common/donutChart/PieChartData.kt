package aeb.proyecto.statistics.components.common.donutChart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class PieChartData(
    val title:String,
    val value:Float,
    val color: Int
)


val listaDePrueba = listOf(
    PieChartData(
        title = "Lunes",
        value = 130f,
        color = Color.Red.toArgb() // Convertimos el Color de Compose a Int
    ),
    PieChartData(
        title = "Martes",
        value = 90f,
        color = Color.Cyan.toArgb()
    ),
    PieChartData(
        title = "Miércoles",
        value = 210f,
        color = Color.Green.toArgb()
    )
)