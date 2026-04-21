package aeb.proyecto.statistics.components.common.donutChart

import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.collections.mutableListOf

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    data: DonutChartDataCollection,
    chartSize: Dp = 350.dp,
    gapPercentage: Float = 0.04f,
    selectionView: @Composable (selectedItem: DonutChartData?) -> Unit = {_ -> },
){

    val anglesList: MutableList<DrawingAngles> = remember { mutableListOf() }
    val gapAngle = data.calculateGapAngle(gapPercentage)

    var selectedIndex by remember { mutableIntStateOf(-1) }
    val animationTargetState = (0..data.items.size).map {
        remember { mutableStateOf(DonutChartState()) }
    }
    val animValues = (0..data.items.size).map {
        animateDpAsState(
            targetValue = animationTargetState[it].value.stroke,
            animationSpec = TweenSpec(700)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing6)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(spacing6)
            )
            .background(MaterialTheme.colorScheme.surfaceTint),
        contentAlignment = Alignment.Center
    ) {
        // 2
        Canvas(
            modifier = Modifier
                .padding(vertical = spacing8)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->

                        }
                    )
                }
                .size(chartSize),
            // 3
            onDraw = {
                val defaultStrokeWidth = STROKE_SIZE_UNSELECTED.toPx()
                anglesList.clear()
                var lastAngle = 0f
                data.items.forEachIndexed { ind, item ->
                    val sweepAngle = data.findSweepAngle(ind, gapPercentage)
                    anglesList.add(DrawingAngles(lastAngle, sweepAngle))
                    // 4
                    drawArc(
                        color = item.color,
                        startAngle = lastAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(defaultStrokeWidth / 2, defaultStrokeWidth / 2),
                        style = Stroke(defaultStrokeWidth, cap = StrokeCap.Butt),
                        size = Size(size.width - defaultStrokeWidth,
                            size.height - defaultStrokeWidth)
                    )
                    // 5
                    lastAngle += sweepAngle + gapAngle
                }
            }
        )
    }
}