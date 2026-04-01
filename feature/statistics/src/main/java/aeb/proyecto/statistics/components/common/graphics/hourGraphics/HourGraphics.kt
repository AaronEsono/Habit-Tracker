package aeb.proyecto.statistics.components.common.graphics.hourGraphics

import aeb.proyecto.statistics.R
import aeb.proyecto.statistics.components.common.graphics.utils.monthLabelKeys
import aeb.proyecto.statistics.model.GraphicsState
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker


// Poner el año
// Hacer label del año

@Composable
fun HourGraphics(
    modifier: Modifier = Modifier,
    graphicsState: GraphicsState = GraphicsState(),
    yearGraphicsSelected: Int,
    onYearSelected: (Boolean) -> Unit = {}
){

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    // --- Lógica Responsive ---
    val isTablet = configuration.screenWidthDp > 600
    val scaleFactor = if (isTablet) 1.4f else 1.0f

    // Estilos de texto adaptativos (puedes usar copy para cambiar solo el tamaño)
    val titleSize = MaterialTheme.typography.titleSmall.fontSize * scaleFactor
    val labelSize = MaterialTheme.typography.labelLarge.fontSize * scaleFactor
    val iconSize = 20.dp * scaleFactor

    // ... (tus definiciones de myLineProvider, monthLabels, etc.)

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
            val hour = x.toInt()
            "%02d:00".format(hour)
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
            Column(
                modifier = Modifier.padding(top = spacing6)
            ){

                TitleSmallText(
                    text = stringResource(R.string.statistics_label_graphics_hour),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = spacing12),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = titleSize
                )

                Spacer(modifier = Modifier.padding(vertical = spacing2))

                Row (
                    Modifier.padding(start = spacing4),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        Icons.Filled.ArrowBackIosNew,
                        contentDescription = "arrow back year selected",
                        tint = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = null
                        ){
                            onYearSelected(false)
                        }.size(iconSize)
                    )

                    LabelLargeText(
                        text = yearGraphicsSelected.toString(),
                        modifier = Modifier.padding(horizontal = spacing2),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = labelSize
                    )

                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "arrow forward year selected",
                        tint = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = null
                        ){
                            onYearSelected(true)
                        }.size(iconSize)
                    )
                }

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
                            labelRotationDegrees = 75f,
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

}