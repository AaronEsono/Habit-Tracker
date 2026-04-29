package aeb.proyecto.statistics.components.horizontal.donutChart

import aeb.proyecto.statistics.components.common.donutChart.ChartCanvas
import aeb.proyecto.statistics.components.common.donutChart.PieChartData
import aeb.proyecto.statistics.components.common.donutChart.PieChartLabel
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HorizontalPieChart(
    modifier: Modifier = Modifier,
    data: List<PieChartData>,
    chartHeight: Dp = 200.dp
){

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing6)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(spacing6))
            .background(MaterialTheme.colorScheme.surfaceTint) // Un toque sutil
    ) {
        AnimatedContent(
            targetState = data,
            transitionSpec = {
                fadeIn(animationSpec = tween(1000)) togetherWith fadeOut(animationSpec = tween(500))
            },
            label = "HorizontalPieChartRotation"
        ) { animatedData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing16),
                verticalAlignment = Alignment.CenterVertically, // Centra el donut con los textos
                horizontalArrangement = Arrangement.spacedBy(spacing24) // Más espacio entre donut y leyenda
            ) {
                // 1. EL GRÁFICO (Izquierda)
                Box(
                    modifier = Modifier
                        .weight(1f), // Toma la mitad o el espacio necesario
                    contentAlignment = Alignment.Center
                ) {
                    ChartCanvas(
                        data = animatedData,
                        modifier = Modifier
                            .size(chartHeight)
                            .aspectRatio(1f)
                    )
                }

                // 2. LAS LEYENDAS (Derecha)
                Column(
                    modifier = Modifier
                        .weight(1.2f), // Un poco más de peso para el texto si es largo
                    verticalArrangement = Arrangement.spacedBy(spacing8, Alignment.CenterVertically)
                ) {
                    animatedData.forEach { pieData ->
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