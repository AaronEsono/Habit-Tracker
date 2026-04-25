package aeb.proyecto.statistics.components.common.donutChart

import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


// Ahora mismo no utilizamos un height, por la tanto, el donut y los textos son muy grandes, hay que tratar ese 0.35 directamente
// al donut, al igual que los textos
// Arreglar tamaños

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PieChart(
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
    ) {
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
                data = data,
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

// Extraemos el Canvas a una función para no repetir código
@Composable
fun ChartCanvas(data: List<PieChartData>, modifier: Modifier) {
    Canvas(modifier = modifier) {
        val dynamicStrokeWidth = size.width * 0.15f // Un poco más grueso para que se vea mejor
        var lastValue = 0f
        val arcSize = Size(size.width - dynamicStrokeWidth, size.height - dynamicStrokeWidth)
        val offset = Offset(dynamicStrokeWidth / 2, dynamicStrokeWidth / 2)

        data.forEachIndexed { index, pieData ->
            drawArc(
                color = Color(data[index].color),
                startAngle = lastValue,
                sweepAngle = pieData.value,
                useCenter = false,
                topLeft = offset,
                size = arcSize,
                style = Stroke(dynamicStrokeWidth, cap = StrokeCap.Butt)
            )
            lastValue += pieData.value
        }
    }
}