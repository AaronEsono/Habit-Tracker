package aeb.proyecto.statistics.components.common.graphics.monthGraphics

import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shape.Shape


private val x = (2010..2023).toList()
private val y = listOf<Number>(0.28, 43, 3.1, 5.8, 15, 12, 29, 39, 0.54, 56, 54, 86, 12, 93)


//Camiar el color de los ejes
//Cambiar color del marker
// Preparar la funcion y documentarlo

@Composable
fun MonthGraphics(){

    val labelColor = Color.White
    val markerColor = Color(0xFF6750A4)

    val label = rememberTextComponent(
        color = Color.Red, // Texto blanco
        padding = Dimensions(
            horizontalDp = 12f, // Espacio lateral
            verticalDp = 2f // Espacio superior/inferior
        ),
    )

    val myLineProvider = LineCartesianLayer.LineProvider.series(
        // Usamos rememberLine que es lo que aparece en tu código fuente
        rememberLine(
            // Definimos el color de la línea
            fill = LineCartesianLayer.LineFill.single(
                fill(Color.Green)
            ),
            thickness = 2.dp // Grosor de la línea
        )
    )

    val marker = rememberDefaultCartesianMarker(
        label = label,
        guideline = rememberLineComponent(Color.LightGray, spacing1)
    )

    val model = remember {
        CartesianChartModel(
            LineCartesianLayerModel.build {
                series(x, y)
            }
        )
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            layers = arrayOf(rememberLineCartesianLayer(
                lineProvider = myLineProvider,
            )),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
            marker = marker
        ),
        model = model, // Pasamos el modelo directamente, no el producer
        modifier = Modifier.padding(horizontal = spacing4)
    )

}