package aeb.proyecto.statistics.components.vertical.donutChart

import aeb.proyecto.statistics.components.common.donutChart.ChartCanvas
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.common.donutChart.PieChartLabel
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Renders a vertical dashboard component featuring a Donut Chart placed above
 * its descriptive legend. This layout is optimized for narrow containers.
 *
 * @param modifier Applied to the outer container.
 * @param data The list of [PieChartData] to be visualized.
 * @param chartHeight The fixed height for the Donut Chart, which
 * dictates the scaling of the legend labels.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun VerticalPieChart(
    modifier: Modifier = Modifier,
    data: List<PieChartData>,
    chartHeight: Dp = 200.dp
){

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing6)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(spacing6))
            .background(MaterialTheme.colorScheme.surfaceTint)
            .testTag("pie_chart_label")
    ) {
        AnimatedContent(
            targetState = data,
            transitionSpec = {
                // Combinamos un fundido de entrada con uno de salida
                // 'tween(1000)' hace que dure exactamente 1 segundo
                fadeIn(
                    animationSpec = tween(1000)
                ) togetherWith fadeOut(
                    animationSpec = tween(500) // La salida puede ser un poco más rápida para no solapar
                )
            },
        ) {animatedData ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing16),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing16)
            ) {
                // 1. EL GRÁFICO (Arriba)
                // Usamos un tamaño relativo al ancho disponible
                ChartCanvas(
                    data = animatedData,
                    modifier = Modifier
                        .size(chartHeight) // Ocupa el 60% del ancho del Box
                        .aspectRatio(1f)     // Mantiene la forma circular
                )

                // 2. LAS LEYENDAS (Abajo)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(spacing4)
                ) {
                    data.forEachIndexed { _, pieData ->
                        PieChartLabel(
                            containerHeight = chartHeight,
                            pieChartData = pieData
                        )
                    }
                }
            }
        }
    }
}