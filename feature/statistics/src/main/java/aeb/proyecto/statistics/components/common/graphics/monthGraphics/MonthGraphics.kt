package aeb.proyecto.statistics.components.common.graphics.monthGraphics

import aeb.proyecto.statistics.components.common.graphics.utils.monthLabelKeys
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
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
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shape.Shape



@Composable
fun MonthGraphics(
    modifier: Modifier = Modifier,
    graphicsState: GraphicsState = GraphicsState()
){

    if(graphicsState.model != null){
        val context = LocalContext.current
        val monthLabels = remember(context) {
            monthLabelKeys.map { context.getString(it) }
        }

        val myLineProvider = LineCartesianLayer.LineProvider.series(
            // Usamos rememberLine que es lo que aparece en tu código fuente
            rememberLine(
                // Definimos el color de la línea
                fill = LineCartesianLayer.LineFill.single(
                    fill(Color(graphicsState.color))
                ),
                thickness = spacing2 // Grosor de la línea
            )
        )

        val bottomAxisValueFormatter = CartesianValueFormatter { x, _, _ ->
            monthLabels.getOrElse(x.toInt()) { "" }
        }

        val marker = DefaultCartesianMarker(
            label = rememberTextComponent(
                color = MaterialTheme.colorScheme.onSurface
            ),
            guideline = rememberLineComponent(MaterialTheme.colorScheme.onSurface, spacing1)
        )


        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = spacing6)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(spacing6)
                )
                .background(MaterialTheme.colorScheme.surfaceTint)
        ) {
            CartesianChartHost(
                chart = rememberCartesianChart(
                    layers = arrayOf(rememberLineCartesianLayer(
                        lineProvider = myLineProvider,
                    )),
                    startAxis = rememberStartAxis(
                        line = rememberAxisLineComponent(
                            color = MaterialTheme.colorScheme.scrim
                        ),
                        label = rememberAxisLabelComponent(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        guideline = rememberAxisGuidelineComponent(
                            color = MaterialTheme.colorScheme.outline
                        )
                    ),
                    bottomAxis = rememberBottomAxis(
                        line = rememberAxisLineComponent(
                            color = MaterialTheme.colorScheme.scrim
                        ),
                        labelRotationDegrees = 45f,
                        label = rememberAxisLabelComponent(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        guideline = rememberAxisGuidelineComponent(
                            color = MaterialTheme.colorScheme.outline
                        ),
                        valueFormatter = bottomAxisValueFormatter
                    ),
                    marker = marker
                ),
                model = graphicsState.model,
                modifier = Modifier.padding(horizontal = spacing6)
            )
        }
    }

}