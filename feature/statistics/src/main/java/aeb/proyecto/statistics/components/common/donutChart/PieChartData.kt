package aeb.proyecto.statistics.components.common.donutChart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class PieChartData(
    val title:String,
    val percentage:Float,
    val value:Float,
    val color: Int
)


val listaDePrueba = listOf(
    PieChartData(
        title = "Lunes",
        value = 130f,
        percentage = 100f,
        color = Color.Red.toArgb() // Convertimos el Color de Compose a Int
    ),
    PieChartData(
        title = "Martes",
        value = 90f,
        percentage = 100f,
        color = Color.Cyan.toArgb()
    ),
    PieChartData(
        title = "Miércoles",
        value = 210f,
        percentage = 100f,
        color = Color.Green.toArgb()
    )
)