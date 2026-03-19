package aeb.proyecto.statistics.components.common.graphics.monthGraphics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer

@Composable
fun MonthGraphics(){
    val modelProducer = remember{ CartesianChartModelProducer() }
}