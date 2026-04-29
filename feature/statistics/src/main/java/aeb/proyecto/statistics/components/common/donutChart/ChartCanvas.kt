package aeb.proyecto.statistics.components.common.donutChart

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun ChartCanvas(
    data: List<PieChartData>, modifier: Modifier
){

    val notDoneColor = MaterialTheme.colorScheme.secondaryContainer

    Canvas(modifier = modifier) {
        val dynamicStrokeWidth = size.width * 0.15f // Un poco más grueso para que se vea mejor
        var currentStartAngle = -90f
        val arcSize = Size(size.width - dynamicStrokeWidth, size.height - dynamicStrokeWidth)
        val offset = Offset(dynamicStrokeWidth / 2, dynamicStrokeWidth / 2)

        data.forEachIndexed { index, pieData ->

            val arcColor = when (pieData.state) {
                PieChartState.COMPLETED -> Color(pieData.habitColor)
                PieChartState.UNCOMPLETED -> Color(pieData.habitColor).copy(alpha = 0.4f)
                PieChartState.NOT_DONE -> notDoneColor
            }

            drawArc(
                color = arcColor,
                startAngle = currentStartAngle,
                sweepAngle = pieData.value,
                useCenter = false,
                topLeft = offset,
                size = arcSize,
                style = Stroke(dynamicStrokeWidth, cap = StrokeCap.Butt)
            )
            currentStartAngle += pieData.value
        }
    }

}